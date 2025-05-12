package com.example.estimationtool.controller;

import com.example.estimationtool.model.enums.Role;
import com.example.estimationtool.service.SubProjectService;
import com.example.estimationtool.toolbox.dto.UserViewDTO;
import com.example.estimationtool.model.Project;
import com.example.estimationtool.service.ProjectService;
import com.example.estimationtool.toolbox.dto.UserWithProjectDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("projects") // Base-URL for alle endpoints i ProjectController
public class ProjectController {

    private final ProjectService projectService;
    private final SubProjectService subProjectService;


    // Dependency injection af ProjectService i konstruktør
    public ProjectController(ProjectService projectService, SubProjectService subProjectService) {
        this.projectService = projectService;
        this.subProjectService = subProjectService;
    }

    private UserViewDTO getCurrentUser(HttpSession session) {
        return (UserViewDTO) session.getAttribute("currentUser");
    }


    //--------------------------------- Hent Create() ----------------------------------

    @GetMapping("/create") // Vis opret formular
    public String showCreateForm(Model model,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {

        UserViewDTO currentUser = getCurrentUser(session);

        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at opret et projekt.");
            return "redirect:/login";
        }

        model.addAttribute("project", new Project());
        return "project/create-project";
    }

    //------------------------------------ Create() ------------------------------------

    @PostMapping("/create") // Opret projekt
    public String createProject(@ModelAttribute Project project,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {

        UserViewDTO currentUser = getCurrentUser(session);

        // Tjekker om brugeren er logget ind
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at oprette et projekt.");
            return "redirect:/login";
        }

        projectService.createProject(currentUser, project);
        redirectAttributes.addFlashAttribute("success", "Projektet er oprettet."); // Viser succesbesked EFTER redirect

        return "redirect:/projects";
    }

    //------------------------------------ Read() --------------------------------------

    @GetMapping("") // Lige nu ser Admin alle projekter og dermed ét specifikt projekt flere gange
    public String showProjectList(Model model,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {

        UserViewDTO currentUser = getCurrentUser(session);

        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at se projekter.");
            return "redirect:/login";
        }

        boolean isAdmin = currentUser.getRole() == Role.ADMIN;
        boolean isProjectManager = currentUser.getRole() == Role.PROJECT_MANAGER;

        List<Project> projectList = isAdmin
                ? projectService.readAll()
                : projectService.readByUserId(currentUser.getUserId());

        if (projectList.isEmpty() && !isAdmin && !isProjectManager) {
            model.addAttribute("info", "Du er ikke tilknyttet nogen projekter endnu");
        }

        UserWithProjectDTO userWithProjectDTO = new UserWithProjectDTO(currentUser, projectList);

        model.addAttribute("userWithProjects", userWithProjectDTO);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("isProjectManager", isProjectManager);

        return "/project/project-list";
    }

    @GetMapping("/{id}")
    public String showProject(@PathVariable int id,
                              Model model,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {

        UserViewDTO currentUser = getCurrentUser(session);

        // Tjekker om brugeren er logget ind
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at se et projekt.");
            return "redirect:/login";
        }

        Project project = projectService.readById(id);
        model.addAttribute("project", project);

        return "project/project-detail";
    }

    // TODO skal endpointet her ikke være projects/{projectId}/subprojects?
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
        boolean isAdmin = currentUser.getRole().equals(Role.ADMIN);
        boolean isProjectManager = currentUser.getRole().equals(Role.PROJECT_MANAGER);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("isProjectManager", isProjectManager);
        model.addAttribute("projectwithsubprojectdto", subProjectService.readAllFromProjectId(projectId));

        return "subproject/subprojects-under-project";
    }

    //------------------------------------ Hent Update() -------------------------------

    @GetMapping("/edit/{id}")
    public String showEditTask(@PathVariable int id,
                               HttpSession session,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        UserViewDTO currentUser = getCurrentUser(session);

        // Tjekker om bruger er logget ind
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at redigere en opgave.");
            return "redirect:/login";
        }

        Project project = projectService.readById(id);

        model.addAttribute("project", project);

        return "project/edit-project";

    }

    //------------------------------------ Update() ------------------------------------

    @PostMapping("/update")
    public String updateTask(@ModelAttribute("project") Project project ,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {

        UserViewDTO currentUser = getCurrentUser(session);

        // Tjekker om bruger er logget ind
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at opdatere et projekt.");
            return "redirect:/login";
        }

        projectService.updateProject(project);

        // Tilføj succesbesked som flash-attribut (vises efter redirect)
        redirectAttributes.addFlashAttribute("success", "Projektet blev opdateret.");

        return "redirect:/projects/" + project.getProjectId();  // Redirect til task-detail
    }

    //------------------------------------ Delete() ------------------------------------

    @PostMapping("/delete/{id}")
    public String deleteTask(@PathVariable int id,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {

        UserViewDTO currentUser = getCurrentUser(session);

        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Du skal være logget ind for at kunne slette et projekt.");
            return "redirect:/login";
        }

        projectService.deleteById(id);

        redirectAttributes.addFlashAttribute("success", "Projektet blev slettet.");

        return "redirect:/projects";
    }


}
