package com.example.estimationtool.controller;

import com.example.estimationtool.dto.UserViewDTO;
import com.example.estimationtool.enums.Role;
import com.example.estimationtool.model.Project;
import com.example.estimationtool.service.ProjectService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

//    @GetMapping("/project")
//    public String getProjectOverview(HttpSession session, RedirectAttributes redirectAttributes, Model model) {
//
//        UserViewDTO currentUser = getCurrentUser(session);
//
//        if (currentUser == null) {
//            redirectAttributes.addFlashAttribute("error", "Du skal være logget ind for at tilgå denne side");
//            return "redirect:/login";
//        }
//
//        model.addAttribute("user", currentUser);
//        return "project/project-overview";
//    }

    //--------------------------------- Hent Create() ----------------------------------

    @GetMapping("/create") // Vis opret formular
    public String showCreateForm(HttpSession session,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {

        UserViewDTO currentUser = getCurrentUser(session);
        if (currentUser == null) return "redirect:/login";

        Role role = currentUser.getRole();
        if (role != Role.ADMIN && role != Role.PROJECT_MANAGER) {
            redirectAttributes.addFlashAttribute("error", "Kun Admin og Projektleder kan oprette et projekt.");
            return "redirect:/error-page";
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

        // Tjekker om brugeren er logget ind
        UserViewDTO currentUser = getCurrentUser(session);
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at oprette et projekt.");
            return "redirect:/login";
        }

        // Rolle validering
        Role role = currentUser.getRole();
        if (role != Role.ADMIN && role != Role.PROJECT_MANAGER) {
            redirectAttributes.addFlashAttribute("error", "Kun Admin og Projektleder kan oprette et projekt.");
            return "redirect:/error-page";
        }

        projectService.createProject(project);

        redirectAttributes.addFlashAttribute("success", "Projektet er oprettet."); // Viser succesbesked EFTER redirect

        return "redirect:/projects/list"; // Kan ikke finde ud af hvor jeg skal redirecte til? :/
    }

    //------------------------------------ Read() --------------------------------------

    @GetMapping("/list") // Vis liste af projekter (Lige nu bare en succesbesked)
    public String showProjectList() {
        return "/project/project-list"; // peger på project-list.html
    }

}
