package com.example.estimationtool.controller;

import com.example.estimationtool.model.SubProject;
import com.example.estimationtool.model.enums.Role;
import com.example.estimationtool.service.ProjectService;
import com.example.estimationtool.service.SubProjectService;
import com.example.estimationtool.toolbox.dto.SubProjectWithTasksDTO;
import com.example.estimationtool.toolbox.dto.SubProjectWithUsersDTO;
import com.example.estimationtool.toolbox.dto.UserViewDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

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

    // TODO - DONE

    @GetMapping("/create")
    public String showCreateSubProject(@RequestParam int projectId,
                                       HttpSession session,
                                       Model model) {

        // Henter og sætter session for Thymeleaf
        UserViewDTO currentUser = getCurrentUser(session);
        session.setAttribute("currentUser", currentUser);

        // Tjekker om brugeren har en aktiv session
        if (currentUser == null) return "redirect:/login";

        // Sender projektId med
        SubProject subProject = new SubProject();
        subProject.setProjectId(projectId);
        model.addAttribute("subproject", subProject);

        return "subproject/create-subproject";
    }

    // TODO - DONE

    @PostMapping("/create")
    public String createSubProject(@ModelAttribute SubProject subProject,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {

        // Henter og sætter session for Thymeleaf
        UserViewDTO currentUser = getCurrentUser(session);
        session.setAttribute("currentUser", currentUser);

        // Tjekker om brugeren har en aktiv session
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at oprette et subprojekt.");
            return "redirect:/login";
        }

        subProjectService.create(currentUser, subProject);
        redirectAttributes.addFlashAttribute("success", "Subprojektet er oprettet.");

        // Redirect til project-with-subprojects.html
        return "redirect:/projects/" + subProject.getProjectId() + "/subprojects";
    }

    //------------------------------------ Read() --------------------------------------

    // TODO - DONE

    @GetMapping("/list")
    public String showAllSubProjects(HttpSession session,
                                     Model model,
                                     RedirectAttributes redirectAttributes) {

        // Henter og sætter session for Thymeleaf
        UserViewDTO currentUser = getCurrentUser(session);
        session.setAttribute("currentUser", currentUser);

        // Tjekker om brugeren har en aktiv session
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at oprette et subprojekt.");
            return "redirect:/login";
        }

        List<SubProject> subProjectList = subProjectService.readAll();
        model.addAttribute("allSubprojects", subProjectList);

        return "subproject/subproject-list";
    }


    // TODO - DONE
    @GetMapping("/{subprojectId}")
    public String showSubProject(HttpSession session,
                                 Model model,
                                 RedirectAttributes redirectAttributes,
                                 @PathVariable int subprojectId) {

        // Henter og sætter session for Thymeleaf
        UserViewDTO currentUser = getCurrentUser(session);
        session.setAttribute("currentUser", currentUser);

        // Tjekker om brugeren har en aktiv session
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at oprette et subprojekt.");
            return "redirect:/login";
        }

        SubProject subProject = subProjectService.readById(subprojectId);
        model.addAttribute("subproject", subProject);

        return "subproject/subproject-detail";
    }

    //------------------------------------ Update() ------------------------------------

    // TODO - DONE

    @GetMapping("/edit/{subprojectId}")
    public String showEditSubProject(HttpSession session,
                                     RedirectAttributes redirectAttributes,
                                     Model model,
                                     @PathVariable int subprojectId) {

        // Henter og sætter session for Thymeleaf
        UserViewDTO currentUser = getCurrentUser(session);
        session.setAttribute("currentUser", currentUser);

        // Tjekker om brugeren har en aktiv session
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at oprette et subprojekt.");
            return "redirect:/login";
        }

        SubProject subProject = subProjectService.readById(subprojectId);
        model.addAttribute("subproject", subProject);

        return "subproject/edit-subproject";
    }

    // TODO - DONE

    @PostMapping("/update")
    public String updateSubProject(HttpSession session,
                                   RedirectAttributes redirectAttributes,
                                   @ModelAttribute("subproject") SubProject subProject) {

        // Henter og sætter session for Thymeleaf
        UserViewDTO currentUser = getCurrentUser(session);
        session.setAttribute("currentUser", currentUser);

        // Tjekker om brugeren har en aktiv session
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at oprette et subprojekt.");
            return "redirect:/login";
        }

        subProjectService.update(currentUser, subProject);
        redirectAttributes.addFlashAttribute("success", "Subprojekt blev opdateret.");

        return "redirect:/subprojects/" + subProject.getSubProjectId();
    }

    //------------------------------------ Delete() ------------------------------------

    // TODO - DONE

    @PostMapping("/delete/{subprojectId}")
    public String deleteSubProject(@PathVariable int subprojectId,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {

        // Henter og sætter session for Thymeleaf
        UserViewDTO currentUser = getCurrentUser(session);
        session.setAttribute("currentUser", currentUser);

        // Tjekker om brugeren har en aktiv session
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Du skal være logget ind for at kunne slette et delprojekt.");
            return "redirect:/login";
        }

        // Henter projektId før sletning for redirect
        SubProject subProject = subProjectService.readById(subprojectId);
        int projectId = subProject.getProjectId();

        subProjectService.deleteById(currentUser, subprojectId);

        redirectAttributes.addFlashAttribute("success", "Subprojektet blev slettet.");

        // Redirect til project-with-subprojects.html
        return "redirect:/projects/" + projectId + "/subprojects";
    }

    //------------------------------------ DTO'er ------------------------------------

    // TODO - DONE

    // --- Viser brugere tilknyttet ét subprojekt ---

    @GetMapping("/{subprojectId}/users")
    public String showSubProjectWithUsers(@PathVariable int subprojectId,
                                          HttpSession session,
                                          Model model,
                                          RedirectAttributes redirectAttributes) {

        // Henter og sætter session for Thymeleaf
        UserViewDTO currentUser = getCurrentUser(session);
        session.setAttribute("currentUser", currentUser);

        // Tjekker at bruger er logget ind
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Du skal være logget ind for at kunne se et delprojekts brugere.");
            return "redirect:/login";
        }

        // Viser allerede tilknyttede brugere
        SubProjectWithUsersDTO subProjectWithUsersDTO = subProjectService.readAllUsersBySubProjectId(subprojectId);
        model.addAttribute("subProjectWithUsers", subProjectWithUsersDTO);

        // Viser ikke-tilknyttede brugere (til POST-formularen)
        List<UserViewDTO> unassignedUsers = subProjectService.readAllUnAssignedUsers(subprojectId);
        model.addAttribute("unassignedUsers", unassignedUsers);

        return "subproject/subproject-with-users";

    }

    // TODO - DONE
    // --- Viser tasks tilknyttet ét subprojekt ---

    @GetMapping("/{subprojectId}/tasks")
    public String showSubProjectWithTasks(@PathVariable int subprojectId,
                                          HttpSession session,
                                          Model model,
                                          RedirectAttributes redirectAttributes) {

        // Henter og sætter session for Thymeleaf
        UserViewDTO currentUser = getCurrentUser(session);
        session.setAttribute("currentUser", currentUser);

        // Tjekker at bruger er logget ind
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Du skal være logget ind for at kunne se et delprojekts opgaver.");
            return "redirect:/login";
        }

        // Henter subprojekt + tilknyttede tasks
        SubProjectWithTasksDTO subProjectWithTasksDTO = subProjectService.readAllTasksBySubProjectId(subprojectId);
        // Tilføjer til model
        model.addAttribute("subProjectWithTasks", subProjectWithTasksDTO);

        return "subproject/subproject-with-tasks";
    }

    //---------------------------- POST Assign User to Subproject -------------------------

    @PostMapping("/subprojects/{subprojectId}/assignusers")
    public String assignUsersToSubProject(@PathVariable int subprojectId,
                                          @RequestParam("userIds") List<Integer> userIds,
                                          HttpSession session,
                                          RedirectAttributes redirectAttributes) {

        // Henter og sætter session for Thymeleaf
        UserViewDTO currentUser = getCurrentUser(session);
        session.setAttribute("currentUser", currentUser);

        // Tjekker sessionen
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Du skal logge ind for at kunne tildele brugere et subprojekt.");
            return "redirect:/login";
        }

        subProjectService.assignUsersToSubProject(currentUser, userIds, subprojectId);

        redirectAttributes.addFlashAttribute("success", "Brugere blev tildelt subprojektet.");
        return "redirect:/subprojects/" + subprojectId + "/users";
    }


}
