package com.example.estimationtool.controller;

import com.example.estimationtool.model.enums.Role;
import com.example.estimationtool.toolbox.dto.UserViewDTO;
import com.example.estimationtool.model.Project;
import com.example.estimationtool.service.ProjectService;
import com.example.estimationtool.toolbox.dto.UserWithProjectDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("projects") // Base-URL for alle endpoints i ProjectController
public class ProjectController {

    private final ProjectService projectService;


    // Dependency injection af ProjectService i konstruktør
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
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

        // Konsol besked til debug
        System.out.println("POST - projektet er created");

        UserViewDTO currentUser = getCurrentUser(session);

        // Tjekker om brugeren er logget ind
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at oprette et projekt.");
            return "redirect:/login";
        }

        projectService.createProject(currentUser, project);
        redirectAttributes.addFlashAttribute("success", "Projektet er oprettet."); // Viser succesbesked EFTER redirect

        return "redirect:/projects/list";
    }

    //------------------------------------ Read() --------------------------------------

    @GetMapping("/list") // Lige nu ser Admin alle projekter og dermed ét specifikt projekt flere gange
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

}
