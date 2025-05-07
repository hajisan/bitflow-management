package com.example.estimationtool.controller;

import com.example.estimationtool.model.SubProject;
import com.example.estimationtool.service.SubProjectService;
import com.example.estimationtool.toolbox.dto.UserViewDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/subprojects")
public class SubProjectController {
    private final SubProjectService subProjectService;

    public SubProjectController(SubProjectService subProjectService) {
        this.subProjectService = subProjectService;
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

        // TODO tilføj en side her, når READ task laves!
        return "";
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

        model.addAttribute("subprojectsUnderProject", subProjectService.readAllFromProjectId(projectId));

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
}
