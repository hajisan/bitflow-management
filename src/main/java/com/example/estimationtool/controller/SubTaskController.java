package com.example.estimationtool.controller;

import com.example.estimationtool.model.SubTask;
import com.example.estimationtool.service.SubTaskService;
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

    public SubTaskController(SubTaskService subTaskService) {
        this.subTaskService = subTaskService;
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
            redirectAttributes.addFlashAttribute("error", "Log ind for at oprette en underopgave.");
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
            redirectAttributes.addFlashAttribute("error", "Log ind for at oprette en underopgave.");
            return "redirect:/login";
        }

        subTaskService.createSubTask(subTask);

        redirectAttributes.addFlashAttribute("success", "Underopgaven blev oprettet");

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
            redirectAttributes.addFlashAttribute("error", "Log ind for at se underopgaver.");
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
            redirectAttributes.addFlashAttribute("error", "Log ind for at se en underopgave");
            return "redirect:/login";
        }

        SubTask subTask = subTaskService.readById(id);

        return "subtask/subtask-detail";

    }
    //------------------------------------ Hent Update() -------------------------------
    //------------------------------------ Update() ------------------------------------
    //------------------------------------ Delete() ------------------------------------

}
