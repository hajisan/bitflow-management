package com.example.estimationtool.controller;

import com.example.estimationtool.model.enums.Role;
import com.example.estimationtool.toolbox.dto.ProjectWithSubProjectsDTO;
import com.example.estimationtool.toolbox.dto.ProjectWithUsersDTO;
import com.example.estimationtool.toolbox.dto.UserViewDTO;
import com.example.estimationtool.model.Project;
import com.example.estimationtool.service.ProjectService;
import com.example.estimationtool.toolbox.dto.UserWithProjectsDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/projects") // Base-URL for alle endpoints i ProjectController
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

    // TODO - DONE
    @GetMapping("/create") // Vis opret formular
    public String showCreateProject(Model model,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {

        // Henter og sætter session for Thymeleaf
        UserViewDTO currentUser = getCurrentUser(session);
        session.setAttribute("currentUser", currentUser);

        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at opret et projekt.");
            return "redirect:/login";
        }

        model.addAttribute("project", new Project());
        return "project/create-project";
    }

    //------------------------------------ Create() ------------------------------------

    // TODO - DONE
    @PostMapping("/create") // Opret projekt
    public String createProject(@ModelAttribute Project project,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {

        // Henter og sætter session for Thymeleaf
        UserViewDTO currentUser = getCurrentUser(session);
        session.setAttribute("currentUser", currentUser);

        // Tjekker om brugeren er logget ind
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at oprette et projekt.");
            return "redirect:/login";
        }

        projectService.createProject(currentUser, project);
        redirectAttributes.addFlashAttribute("success", "Projektet er oprettet."); // Viser succesbesked EFTER redirect

        // Redirect til front-page.html
        return "redirect:/users/profile";
    }

    //------------------------------------ Read() --------------------------------------

    @GetMapping("/list")
    public String showAllProjects(HttpSession session,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {

        // Henter og sætter session for Thymeleaf
        UserViewDTO currentUser = getCurrentUser(session);
        session.setAttribute("currentUser", currentUser);

        // Tjekker om brugeren er logget ind
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at se brugeroplysninger.");
            return "redirect:/login";
        }


        List<Project> projectList = projectService.readAll(currentUser);
        model.addAttribute("projectList", projectList);

        return "project/project-list";
    }


    // TODO - DONE
    @GetMapping("/{projectId}")
    public String showProject(@PathVariable int projectId,
                              Model model,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {

        // Henter og sætter session for Thymeleaf
        UserViewDTO currentUser = getCurrentUser(session);
        session.setAttribute("currentUser", currentUser);

        // Tjekker om brugeren er logget ind
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at se et projekt.");
            return "redirect:/login";
        }

        Project project = projectService.readById(projectId);
        model.addAttribute("project", project);

        return "project/project-detail";
    }

    //------------------------------------ Hent Update() -------------------------------

    // TODO - DONE
    @GetMapping("/edit/{projectId}")
    public String showEditProject(@PathVariable int projectId,
                                  HttpSession session,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {

        // Henter og sætter session for Thymeleaf
        UserViewDTO currentUser = getCurrentUser(session);
        session.setAttribute("currentUser", currentUser);

        // Tjekker om bruger er logget ind
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at redigere en opgave.");
            return "redirect:/login";
        }


        Project project = projectService.readById(projectId);
        model.addAttribute("project", project);

        return "project/edit-project";

    }

    //------------------------------------ Update() ------------------------------------

    // TODO - DONE
    @PostMapping("/update")
    public String updateProject(@ModelAttribute("project") Project project,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {

        // Henter og sætter session for Thymeleaf
        UserViewDTO currentUser = getCurrentUser(session);
        session.setAttribute("currentUser", currentUser);

        // Tjekker om bruger er logget ind
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at opdatere et projekt.");
            return "redirect:/login";
        }

        projectService.updateProject(currentUser, project);

        redirectAttributes.addFlashAttribute("success", "Projektet blev opdateret.");

        return "redirect:/projects/" + project.getProjectId();  // Redirect til project-detail
    }

    //------------------------------------ Delete() ------------------------------------

    @PostMapping("/delete/{projectId}")
    public String deleteProject(@PathVariable int projectId,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {

        // Henter og sætter session for Thymeleaf
        UserViewDTO currentUser = getCurrentUser(session);
        session.setAttribute("currentUser", currentUser);

        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Du skal være logget ind for at kunne slette et projekt.");
            return "redirect:/login";
        }


        projectService.deleteById(projectId, currentUser);

        redirectAttributes.addFlashAttribute("success", "Projektet blev slettet.");

        return "redirect:/projects/list";
    }


    //------------------------------------ DTO'er ------------------------------------


    // --- Viser brugere tilknyttet/ikke-tilknyttet ét projekt ---

    // TODO - DONE
    @GetMapping("/{projectId}/users")
    public String showProjectWithUsers(@PathVariable int projectId,
                                       HttpSession session,
                                       Model model,
                                       RedirectAttributes redirectAttributes) {

        // Henter og sætter session for Thymeleaf
        UserViewDTO currentUser = getCurrentUser(session);
        session.setAttribute("currentUser", currentUser);

        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Du skal være logget ind for at kunne se brugere til et projekt.");
            return "redirect:/login";
        }

        // Viser allerede tilknyttede brugere
        ProjectWithUsersDTO projectWithUsers = projectService.readALlUsersByProjectId(projectId);
        model.addAttribute("projectWithUsers", projectWithUsers);

        // Viser ikke-tilknyttede brugere (til POST-formularen)
        List<UserViewDTO> unassignedUsers = projectService.readAllUnAssignedUsers(projectId);
        model.addAttribute("unassignedUsers", unassignedUsers);

        return "project/project-with-users";
    }

    // --- Viser subprojekter tilknyttet ét projekt ---

    // TODO - DONE
    @GetMapping("/{projectId}/subprojects")
    public String showProjectWithSubProjects(@PathVariable int projectId,
                                          HttpSession session,
                                          Model model,
                                          RedirectAttributes redirectAttributes) {

        // Henter og sætter session for Thymeleaf
        UserViewDTO currentUser = getCurrentUser(session);
        session.setAttribute("currentUser", currentUser);

        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Du skal være logget ind for at kunne se brugere til et projekt.");
            return "redirect:/login";
        }

        // Henter subprojekter for ét projekt
        ProjectWithSubProjectsDTO projectWithSubProjectsDTO = projectService.readAllFromProjectId(projectId);
        model.addAttribute("projectWithSubProjects", projectWithSubProjectsDTO);

        return "project/project-with-subprojects";
    }


    //---------------------------- POST Assign User to Project ----------------------------

    @PostMapping("/{projectId}/assignusers")
    public String assignUsersToProject(@PathVariable int projectId,
                                       @RequestParam("userIds") List<Integer> userIds,
                                       HttpSession session,
                                       RedirectAttributes redirectAttributes) {

        // Henter og sætter session for Thymeleaf
        UserViewDTO currentUser = getCurrentUser(session);
        session.setAttribute("currentUser", currentUser);

        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Du skal være logget ind for at kunne tildele brugere et projekt.");
            return "redirect:/login";
        }

        projectService.assignUserToProject(currentUser,userIds, projectId);

        redirectAttributes.addFlashAttribute("success", "Bruger(e) blev tildelt projektet.");

        return "redirect:/projects/" + projectId + "/users";
    }



}
