package com.example.estimationtool.controller;

import com.example.estimationtool.model.Project;
import com.example.estimationtool.model.SubProject;
import com.example.estimationtool.model.enums.Role;
import com.example.estimationtool.model.enums.Status;
import com.example.estimationtool.service.ProjectService;
import com.example.estimationtool.service.SubProjectService;
import com.example.estimationtool.service.UserService;
import com.example.estimationtool.toolbox.dto.ProjectWithUsersDTO;
import com.example.estimationtool.toolbox.dto.SubProjectWithTasksDTO;
import com.example.estimationtool.toolbox.dto.SubProjectWithUsersDTO;
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
    private final ProjectService projectService;

    public SubProjectController(SubProjectService subProjectService, ProjectService projectService) {
        this.subProjectService = subProjectService;
        this.projectService = projectService;

    }

    private UserViewDTO getCurrentUser(HttpSession session) {
        return (UserViewDTO) session.getAttribute("currentUser");
    }

    //------------------------------------ Create() ------------------------------------

    @GetMapping("/create")
    public String showCreateSubProject(HttpSession session,
                                       Model model) {

        UserViewDTO currentUser = getCurrentUser(session);
        if (currentUser == null) return "redirect:/login";

        model.addAttribute("allProjects", new ArrayList<>(projectService.readAll()));
        model.addAttribute("subproject", new SubProject());
        return "subproject/create-subproject";
    }

    @PostMapping("/create")
    public String createSubProject(@ModelAttribute SubProject subProject,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {

        // Tjekker om brugeren har en aktiv session
        UserViewDTO currentUser = getCurrentUser(session);
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at oprette et delprojekt.");
            return "redirect:/login";
        }

        subProjectService.create(currentUser, subProject);
        redirectAttributes.addFlashAttribute("success", "Delprojektet er oprettet.");

        return "subproject/subproject-list";
    }

    //------------------------------------ Read() --------------------------------------

    @GetMapping("")
    public String showAllSubProjects(HttpSession session,
                                     Model model,
                                     RedirectAttributes redirectAttributes) {

        UserViewDTO currentUser = getCurrentUser(session);
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at oprette et delprojekt.");
            return "redirect:/login";
        }

        boolean isAdmin = currentUser.getRole().equals(Role.ADMIN);
        boolean isProjectManager = currentUser.getRole().equals(Role.PROJECT_MANAGER);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("isProjectManager", isProjectManager);
        model.addAttribute("allSubprojects", subProjectService.readAll());

        return "subproject/subproject-list";
    }



    @GetMapping("/{id}")
    public String showSubProject(HttpSession session,
                                 Model model,
                                 RedirectAttributes redirectAttributes,
                                 @PathVariable int id) {


        UserViewDTO currentUser = getCurrentUser(session);
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at oprette et delprojekt.");
            return "redirect:/login";
        }

        model.addAttribute("subproject", subProjectService.readById(id));

        return "subproject/subproject-details";
    }

    //------------------------------------ Update() ------------------------------------

    @GetMapping("/edit/{id}")
    public String showEditSubProject(HttpSession session,
                                     RedirectAttributes redirectAttributes,
                                     Model model,
                                     @PathVariable int id) {
        UserViewDTO currentUser = getCurrentUser(session);
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at oprette et delprojekt.");
            return "redirect:/login";
        }
        model.addAttribute("allProjects", new ArrayList<>(projectService.readAll()));
        model.addAttribute("subproject", subProjectService.readById(id));

        return "subproject/edit-subproject";
    }

    @PostMapping("/update")
    public String updateSubProject(HttpSession session,
                                   RedirectAttributes redirectAttributes,
                                   Model model, int id,
                                   @RequestParam int newProjectId,
                                   @RequestParam String newName,
                                   @RequestParam String newDescription,
                                   @RequestParam LocalDate newDeadline,
                                   @RequestParam int newEstimatedTime,
                                   @RequestParam int newTimeSpent,
                                   @RequestParam Status newStatus) {

        UserViewDTO currentUser = getCurrentUser(session);
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at oprette et delprojekt.");
            return "redirect:/login";
        }

        model.addAttribute("oldsubproject", subProjectService.readById(id));
        model.addAttribute("updatedsubproject", subProjectService.update(currentUser, new SubProject(
                newProjectId, newEstimatedTime, newTimeSpent, newName, newDescription, newDeadline, newStatus
        )));

        redirectAttributes.addFlashAttribute("success", "Delprojekt blev opdateret.");

        return "redirect:/subproject/subproject-details";
    }

    //------------------------------------ Delete() ------------------------------------

    @PostMapping("/delete/{id}")
    public String deleteSubProject(@PathVariable int id,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {

        UserViewDTO currentUser = getCurrentUser(session);

        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Du skal være logget ind for at kunne slette et delprojekt.");
            return "redirect:/login";
        }

        subProjectService.deleteById(currentUser, id);

        redirectAttributes.addFlashAttribute("success", "Delprojektet blev slettet.");

        return "redirect:/projects/list";
    }

    //------------------------------------ DTO'er ------------------------------------


    // --- Viser brugere tilknyttet ét subprojekt ---

    @GetMapping("/{id}/users")
    public String showSubProjectWithUsers(@PathVariable int id,
                                          HttpSession session,
                                          Model model,
                                          RedirectAttributes redirectAttributes) {

        // Tjekker at bruger er logget ind
        UserViewDTO currentUser = getCurrentUser(session);

        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Du skal være logget ind for at kunne se et delprojekts brugere.");
            return "redirect:/login";
        }

        // Viser allerede tilknyttede brugere
        SubProjectWithUsersDTO subProjectWithUsersDTO = subProjectService.readAllUsersBySubProjectId(id);

        // Viser ikke-tilknyttede brugere (til POST-formularen)
        List<UserViewDTO> unassignedUsers = subProjectService.readAllUnAssignedUsers(id);

        model.addAttribute("subProjectWithUsers", subProjectWithUsersDTO);
        model.addAttribute("unassignedUsers", unassignedUsers);
        return "subproject/subproject-with-users";

    }

    // --- Viser tasks tilknyttet ét subprojekt ---

    @GetMapping("/{id}/tasks")
    public String showSubProjectWithTasks(@PathVariable int id,
                                          HttpSession session,
                                          Model model,
                                          RedirectAttributes redirectAttributes) {
        // Tjekker at bruger er logget ind
        UserViewDTO currentUser = getCurrentUser(session);

        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Du skal være logget ind for at kunne se et delprojekts opgaver.");
            return "redirect:/login";
        }

        // Henter subprojekt + tilknyttede tasks
        SubProjectWithTasksDTO subProjectWithTasksDTO = subProjectService.readAllTasksBySubProjectId(id);

        // Tilføjer til model
        model.addAttribute("subProjectWithTasks", subProjectWithTasksDTO);

        return "subproject/subproject-with-tasks";
    }

    //---------------------------- POST Assign User to Subproject -------------------------

    @PostMapping("/subprojects/{id}/assignusers")
    public String assignUsersToSubProject(@PathVariable int id,
                                          @RequestParam("userIds") List<Integer> userIds,
                                          HttpSession session,
                                          RedirectAttributes redirectAttributes) {

        UserViewDTO currentUser = getCurrentUser(session);
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Du skal logge ind for at kunne tildele brugere et subprojekt.");
            return "redirect:/login";
        }

        subProjectService.assignUsersToSubProject(currentUser, userIds, id);

        redirectAttributes.addFlashAttribute("success", "Brugere blev tildelt subprojektet.");
        return "redirect:/subprojects/" + id + "/users";
    }





// TODO LIGGER I PROJECTCONTROLLER NU
    // TODO skal endpointet her ikke være projects/{projectId}/subprojects?
//
//    @GetMapping("/{projectId}/subprojects")
//    public String readByProjectId(HttpSession session,
//                                  Model model,
//                                  RedirectAttributes redirectAttributes,
//                                  @PathVariable int projectId) {
//        UserViewDTO currentUser = getCurrentUser(session);
//        if (currentUser == null) {
//            redirectAttributes.addFlashAttribute("error", "Log ind for at oprette et delprojekt.");
//            return "redirect:/login";
//        }
//        boolean isAdmin = currentUser.getRole().equals(Role.ADMIN);
//        boolean isProjectManager = currentUser.getRole().equals(Role.PROJECT_MANAGER);
//        model.addAttribute("isAdmin", isAdmin);
//        model.addAttribute("isProjectManager", isProjectManager);
//        model.addAttribute("projectwithsubprojectdto", subProjectService.readAllFromProjectId(projectId));
//
//        return "subproject/subprojects-under-project";
//    }

}
