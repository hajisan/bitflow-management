package com.example.estimationtool.controller;

import com.example.estimationtool.model.SubTask;
import com.example.estimationtool.model.enums.Status;
import com.example.estimationtool.service.SubTaskService;
import com.example.estimationtool.service.UserService;
import com.example.estimationtool.toolbox.dto.SubTaskWithTimeEntriesDTO;
import com.example.estimationtool.toolbox.dto.UserViewDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("subtasks")
public class SubTaskController {

    private final SubTaskService subTaskService;
    private final UserService userService;

    public SubTaskController(SubTaskService subTaskService, UserService userService) {
        this.subTaskService = subTaskService;
        this.userService = userService;
    }

    //------------------------------------ Sætter sessionen ----------------------------


    private UserViewDTO getCurrentUser(HttpSession session) {
        return (UserViewDTO) session.getAttribute("currentUser");
    }

    //------------------------------------ Hent Create() -------------------------------


    @GetMapping("/create")
    public String showCreateSubTask(Model model,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes
                                  ) {
        UserViewDTO currentUser = getCurrentUser(session);

        // Tjekker om brugeren er logget ind
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at oprette en subtask.");
            return "redirect:/login";
        }

        model.addAttribute("subtask", new SubTask());

        return "subtask/create-subtask";
    }

    //------------------------------------ Create() ------------------------------------

    @PostMapping("/create")
    public String createSubTask(@ModelAttribute("subtask") SubTask subTask,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {

        UserViewDTO currentUser = getCurrentUser(session);

        // Tjekker om brugeren er logget ind
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at oprette en subtask.");
            return "redirect:/login";
        }

        subTaskService.createSubTask(subTask);

        redirectAttributes.addFlashAttribute("success", "Subtask blev oprettet.");

        return "redirect:/subtasks/subtasks";

    }

    //------------------------------------ Read() --------------------------------------

    @GetMapping("subtasks")

    public String showAllSubTasks(Model model,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {

        UserViewDTO currentUser = getCurrentUser(session);

        // Tjekker om brugeren er logget ind
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at se alle subtasks.");
            return "redirect:/login";
        }

        List<SubTask> subTaskList = subTaskService.readAll();

        model.addAttribute("subtasks", subTaskList);

        return "subtask/subtask-list";

    }

    @GetMapping("/{id}")
    public String showSubTask(@PathVariable int id,
                              Model model,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {

        UserViewDTO currentUser = getCurrentUser(session);

        // Tjekker om brugeren er logget ind
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at se en subtask.");
            return "redirect:/login";
        }

        // Henter alle subtasks
        SubTask subTask = subTaskService.readById(id);

        // Henter alle brugere (for dropdown menu)
        List<UserViewDTO> allUserList = userService.readAll();

        // Henter brugeren tilknyttet subtask
        UserViewDTO assignedUser = subTaskService.readAssignedUserBySubTaskId(id); // denne metode laver vi straks


        model.addAttribute("subtask", subTask);
        model.addAttribute("allUserList", allUserList);
        model.addAttribute("assignedUser", assignedUser);

        return "subtask/subtask-detail";

    }
    //------------------------------------ Hent Update() -------------------------------

    @GetMapping("/edit/{id}")
    public String showEditSubTask(@PathVariable int id,
                                  Model model,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {

        UserViewDTO currentUser = getCurrentUser(session);

        // Tjekker om brugeren er logget ind
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at redigere en subtask.");
            return "redirect:/login";
        }

        SubTask subTask = subTaskService.readById(id);

        model.addAttribute("subtask", subTask);
        model.addAttribute("statuses", Status.values()); //Fordi Thymeleaf ikke vil læse vores enum

        return "subtask/edit-subtask";
    }

    //------------------------------------ Update() ------------------------------------

    @PostMapping("/update")
    public String updateSubTask(@ModelAttribute("subtask") SubTask subTask,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {

        UserViewDTO currentUser = getCurrentUser(session);

        // Tjekker om bruger er logget ind
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at opdatere en subtask.");
            return "redirect:/login";
        }

        subTaskService.updateSubTask(subTask);

        // Tilføj succesbesked som flash-attribut (vises efter redirect)
        redirectAttributes.addFlashAttribute("success", "Underopgaven blev opdateret.");

        return "redirect:/subtasks/" + subTask.getSubTaskId(); // Redirect til subtask-detail.html
    }

    //------------------------------------ Delete() ------------------------------------

    @PostMapping("/delete/{id}")
    public String deleteSubTask(@PathVariable int id,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {

        UserViewDTO currentUser = getCurrentUser(session);

        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Du skal være logget ind for at kunne slette en subtask.");
            return "redirect:/login";
        }

        subTaskService.deleteById(id);

        redirectAttributes.addFlashAttribute("success", "Subtask blev slettet.");

        return "redirect:/subtasks/subtasks";
    }

    //---------------------------------- DTO read() ------------------------------------


    // -------------------- Viser en subtask's tilknyttede timeEntries ----------------

    @GetMapping("/{id}/timeentries")
    public String showSubTaskWithTimeEntries(@PathVariable int id,
                                             HttpSession session,
                                             Model model,
                                             RedirectAttributes redirectAttributes) {

        UserViewDTO currentUser = getCurrentUser(session);
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Du skal være logget ind for at se tidsregistreringer for en subtask.");
            return "redirect:/login";
        }

        SubTaskWithTimeEntriesDTO dto = subTaskService.readAllTimeEntriesBySubTaskId(id);
        model.addAttribute("subTaskWithTimeEntries", dto);

        return "subtask/subtask-with-timeentries";
    }

    //------------------------- POST Assign mig selv til SubTask -----------------------

    @PostMapping("/{id}/assignme")
    public String assignSelfToSubTask(@PathVariable int id,
                                      HttpSession session,
                                      RedirectAttributes redirectAttributes) {

        UserViewDTO currentUser = (UserViewDTO) session.getAttribute("currentUser");
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Du skal være logget ind.");
            return "redirect:/login";
        }

        subTaskService.assignUserToSubTask(currentUser, currentUser.getUserId(), id);

        redirectAttributes.addFlashAttribute("success", "Du er nu tildelt denne subtask.");
        return "redirect:/subtasks/" + id;

    }

    //------------------------- POST Assign User til SubTask --------------------------

    @PostMapping("/{id}/assign")
    public String assignOtherUserToSubTask(@PathVariable int id,
                                           @RequestParam("userId") int userId,
                                           HttpSession session,
                                           RedirectAttributes redirectAttributes) {

        UserViewDTO currentUser = (UserViewDTO) session.getAttribute("currentUser");
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Login kræves.");
            return "redirect:/login";
        }

        subTaskService.assignUserToSubTask(currentUser, userId, id);

        redirectAttributes.addFlashAttribute("success", "Subtasken blev tildelt brugeren.");
        return "redirect:/subtasks/" + id;
    }





}
