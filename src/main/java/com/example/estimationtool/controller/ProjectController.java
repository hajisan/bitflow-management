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
    public String showCreateProject(Model model,
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

        return "redirect:/projects/list";
    }

    //------------------------------------ Read() --------------------------------------

    @GetMapping("/list")
    public String showAllProjects(HttpSession session,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {

        UserViewDTO currentUser = getCurrentUser(session);

        // Tjekker om brugeren er logget ind
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at se brugeroplysninger.");
            return "redirect:/login";
        }

        List<Project> projectList = projectService.readAll();

        model.addAttribute("projectList", projectList);

        return "project/project-list";
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

    //------------------------------------ Hent Update() -------------------------------

    @GetMapping("/edit/{id}")
    public String showEditProject(@PathVariable int id,
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
    public String updateProject(@ModelAttribute("project") Project project,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {

        UserViewDTO currentUser = getCurrentUser(session);

        // Tjekker om bruger er logget ind
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at opdatere et projekt.");
            return "redirect:/login";
        }

        projectService.updateProject(currentUser, project);

        // Tilføj succesbesked som flash-attribut (vises efter redirect)
        redirectAttributes.addFlashAttribute("success", "Projektet blev opdateret.");

        return "redirect:/projects/" + project.getProjectId();  // Redirect til task-detail
    }

    //------------------------------------ Delete() ------------------------------------

    @PostMapping("/delete/{id}")
    public String deleteProject(@PathVariable int id,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {

        UserViewDTO currentUser = getCurrentUser(session);

        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Du skal være logget ind for at kunne slette et projekt.");
            return "redirect:/login";
        }

        projectService.deleteById(id);

        redirectAttributes.addFlashAttribute("success", "Projektet blev slettet.");

        return "redirect:/projects/list";
    }


    //------------------------------------ DTO'er ------------------------------------


    // --- Viser brugere tilknyttet/ikke-tilknyttet ét projekt ---

    @GetMapping("/{id}/users")
    public String showProjectWithUsers(@PathVariable int id,
                                       HttpSession session,
                                       Model model,
                                       RedirectAttributes redirectAttributes) {

        UserViewDTO currentUser = getCurrentUser(session);

        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Du skal være logget ind for at kunne se brugere til et projekt.");
            return "redirect:/login";
        }

        // Viser allerede tilknyttede brugere
        ProjectWithUsersDTO projectWithUsers = projectService.readALlUsersByProjectId(id);

        // Viser ikke-tilknyttede brugere (til POST-formularen)
        List<UserViewDTO> unassignedUsers = projectService.readAllUnAssignedUsers(id);

        model.addAttribute("projectWithUsers", projectWithUsers);
        model.addAttribute("unassignedUsers", unassignedUsers);

        return "project/project-with-users";
    }

    // --- Viser subprojekter tilknyttet ét projekt ---

    @GetMapping("/{id}/subprojects")
    public String showProjectWithSubProjects(@PathVariable int id,
                                          HttpSession session,
                                          Model model,
                                          RedirectAttributes redirectAttributes) {

        UserViewDTO currentUser = getCurrentUser(session);

        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Du skal være logget ind for at kunne se brugere til et projekt.");
            return "redirect:/login";
        }

        // Henter subprojekter for ét projekt
        ProjectWithSubProjectsDTO projectWithSubProjectsDTO = projectService.readAllFromProjectId(id);

        model.addAttribute("projectWithSubProjects", projectWithSubProjectsDTO);

        return "project/project-with-subprojects";
    }


    //---------------------------- POST Assign User to Project ----------------------------

    @PostMapping("/{id}/assignusers")
    public String assignUsersToProject(@PathVariable int id,
                                       @RequestParam("userIds") List<Integer> userIds,
                                       HttpSession session,
                                       RedirectAttributes redirectAttributes) {

        UserViewDTO currentUser = getCurrentUser(session);

        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Du skal være logget ind for at kunne tildele brugere et projekt.");
            return "redirect:/login";
        }

        projectService.assignUserToProject(currentUser,userIds, id);

        redirectAttributes.addFlashAttribute("success", "Bruger(e) blev tildelt projektet.");

        return "redirect:/projects/" + id + "/users";
    }



}
