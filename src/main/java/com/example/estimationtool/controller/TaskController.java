package com.example.estimationtool.controller;

import com.example.estimationtool.dto.UserViewDTO;
import com.example.estimationtool.model.Task;
import com.example.estimationtool.service.TaskService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("tasks")
public class TaskController {

    private final TaskService taskService;

    // Dependency Injection af TaskService i constructoren
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }


    private UserViewDTO getCurrentUser(HttpSession session) {
        return (UserViewDTO) session.getAttribute("currentUser");
    }

    //------------------------------------ Hent Create() -------------------------------

    @GetMapping("/create")
    public String showTask(Model model,
                           HttpSession session,
                           RedirectAttributes redirectAttributes
                           ) {

        UserViewDTO currentUser = getCurrentUser(session);

        // Tjekker om brugeren er logget ind
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at se en opgave.");
            return "redirect:/login";
        }

        model.addAttribute("task", new Task());
        return "task/create-task";
    }

    //------------------------------------ Create() ------------------------------------

    @PostMapping("/create")
    public String createTask(@ModelAttribute("task") Task task,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {

        UserViewDTO currentUser = getCurrentUser(session);

        // Tjekker om brugeren er logget ind
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at oprette en ny opgave.");
            return "redirect:/login";
        }

        taskService.createTask(task);

        redirectAttributes.addFlashAttribute("success", "Opgaven blev oprettet");

        return "redirect:/tasks/tasks";

        }

    //------------------------------------ Read() --------------------------------------

    @GetMapping("tasks")
    public String showAllTasks(Model model,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {

        UserViewDTO currentUser = getCurrentUser(session);

        // Tjekker om brugeren er logget ind
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at se opgaver.");
            return "redirect:/login";
        }

        List<Task> taskList = taskService.readAll();

        model.addAttribute("tasks", taskList);

        return "task/task-list";

    }

    @GetMapping("/{id}")
    public String showTask(@PathVariable int id,
                           Model model,
                           HttpSession session,
                           RedirectAttributes redirectAttributes
                           ) {
        UserViewDTO currentUser = getCurrentUser(session);

        // Tjekker om brugeren er logget ind
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at se en opgave.");
            return "redirect:/login";
        }

        Task task = taskService.readById(id);
        model.addAttribute("task", task);

        return "task/task-detail";

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
            redirectAttributes.addFlashAttribute("error", "Log ind for at opdatere en opgave.");
            return "redirect:/login";
        }

        Task task = taskService.readById(id);

        model.addAttribute("task", task);

        return "task/edit-task";

    }
    //------------------------------------ Update() ------------------------------------

    @PostMapping("/edit")
    public String updateTask(@ModelAttribute("task") Task task,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {

        UserViewDTO currentUser = getCurrentUser(session);

        // Tjekker om bruger er logget ind
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at opdatere en opgave.");
            return "redirect:/login";
        }

        taskService.updateTask(task);

        // Tilføj succesbesked som flash-attribut (vises efter redirect)
        redirectAttributes.addFlashAttribute("success", "Opgaven blev opdateret.");

        return "redirect:/tasks/" + task.getTaskId(); // Redirect til task-detail

    }
    //------------------------------------ Delete() ------------------------------------

    @PostMapping("/delete/{id}")
    public String deleteTask(@PathVariable int id,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {

        UserViewDTO currentUser = getCurrentUser(session);

        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Du skal være logget ind for at kunne slette opgaven.");
            return "redirect:/login";
        }

        taskService.deleteById(id);

        redirectAttributes.addFlashAttribute("success", "Opgaven blev slettet.");

        return "redirect:/tasks/tasks";
    }


}




