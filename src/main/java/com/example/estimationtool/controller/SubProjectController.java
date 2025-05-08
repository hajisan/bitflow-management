package com.example.estimationtool.controller;

import com.example.estimationtool.model.Project;
import com.example.estimationtool.model.SubProject;
import com.example.estimationtool.model.enums.Status;
import com.example.estimationtool.service.ProjectService;
import com.example.estimationtool.service.SubProjectService;
import com.example.estimationtool.toolbox.dto.UserViewDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/subprojects")
public class SubProjectController {
    private final SubProjectService subProjectService;
    private final ProjectController projectController;
    private final ProjectService projectService;

    public SubProjectController(SubProjectService subProjectService, ProjectController projectController, ProjectService projectService) {
        this.subProjectService = subProjectService;
        this.projectController = projectController;
        this.projectService = projectService;
    }

    private UserViewDTO getCurrentUser(HttpSession session) {
        return (UserViewDTO) session.getAttribute("currentUser");
    }

    //------------------------------------ Create() ------------------------------------
    @GetMapping("/create")
    public String getCreateSubProject(HttpSession session,
                                      Model model) {
        UserViewDTO currentUser = getCurrentUser(session);
        if (currentUser == null) return "redirect:/login";
        model.addAttribute("allProjects", new ArrayList<>(projectService.readAll()));
        model.addAttribute("subproject", new SubProject());
        return "subproject/create-subproject";
    }

    @PostMapping("/create")
    public String postCreateSubProject(@ModelAttribute SubProject subProject,
                                       HttpSession session,
                                       RedirectAttributes redirectAttributes) {

        // Tjekker om brugeren har en aktiv session
        UserViewDTO currentUser = getCurrentUser(session);
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at oprette et projekt.");
            return "redirect:/login";
        }

        subProjectService.create(currentUser, subProject);
        redirectAttributes.addFlashAttribute("success", "Subprojektet er oprettet.");

        return "subproject/subproject-list";
    }

    //------------------------------------ Read() --------------------------------------
    @GetMapping("")
    public String readAllSubProjects(HttpSession session,
                                     Model model,
                                     RedirectAttributes redirectAttributes) {
        UserViewDTO currentUser = getCurrentUser(session);
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at oprette et projekt.");
            return "redirect:/login";
        }

        model.addAttribute("allSubprojects", subProjectService.readAll());

        return "subproject/subproject-list";
    }

    @GetMapping("/{projectId}/subprojects")
    public String readByProjectId(HttpSession session,
                                  Model model,
                                  RedirectAttributes redirectAttributes,
                                  @PathVariable int projectId) {
        UserViewDTO currentUser = getCurrentUser(session);
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at oprette et projekt.");
            return "redirect:/login";
        }
        model.addAttribute("projectwithsubprojectdto", subProjectService.readAllFromProjectId(projectId));

        return "subproject/subprojects-under-project";
    }

    @GetMapping("/{id}")
    public String readById(HttpSession session,
                           Model model,
                           RedirectAttributes redirectAttributes,
                           @PathVariable int id) {
        UserViewDTO currentUser = getCurrentUser(session);
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at oprette et projekt.");
            return "redirect:/login";
        }

        model.addAttribute("subproject", subProjectService.readById(id));

        return "subproject/subproject-details";
    }

    //------------------------------------ Update() ------------------------------------
    @GetMapping("/{id}/edit")
    public String getUpdateSubProject(HttpSession session,
                                      RedirectAttributes redirectAttributes,
                                      Model model,
                                      @PathVariable int id) {
        UserViewDTO currentUser = getCurrentUser(session);
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at oprette et projekt.");
            return "redirect:/login";
        }

        model.addAttribute("subproject", subProjectService.readById(id));

        return "subproject/edit-subproject";
    }

    @PostMapping("/{id}/update")
    public String postUpdateSubProject(HttpSession session,
                                       RedirectAttributes redirectAttributes,
                                       Model model,
                                       @PathVariable int id,
                                       @RequestParam int newProjectId,
                                       @RequestParam String newName,
                                       @RequestParam String newDescription,
                                       @RequestParam LocalDate newDeadline,
                                       @RequestParam int newEstimatedTime,
                                       @RequestParam int newTimeSpent,
                                       @RequestParam Status newStatus) {

        UserViewDTO currentUser = getCurrentUser(session);
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at oprette et projekt.");
            return "redirect:/login";
        }

        model.addAttribute("oldsubproject", subProjectService.readById(id));
        model.addAttribute("updatedsubproject", subProjectService.update(currentUser, new SubProject(
                newProjectId, newEstimatedTime, newTimeSpent, newName, newDescription, newDeadline, newStatus
        )));

        return "redirect:/subproject/subproject-details";
    }
}
