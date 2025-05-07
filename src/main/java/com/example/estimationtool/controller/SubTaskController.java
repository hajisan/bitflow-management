package com.example.estimationtool.controller;

import com.example.estimationtool.model.SubTask;
import com.example.estimationtool.model.Task;
import com.example.estimationtool.service.SubTaskService;
import com.example.estimationtool.toolbox.dto.UserViewDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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


    @GetMapping("")
    public String showCreateSubTask(Model model,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes
                                  ) {
        UserViewDTO currentUser = getCurrentUser(session);

        // Tjekker om brugeren er logget ind
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at se en opgave.");
            return "redirect:/login";
        }

        model.addAttribute("subtask", new SubTask());

        return "subtask/create-subtask";
    }

    //------------------------------------ Create() ------------------------------------
    //------------------------------------ Read() --------------------------------------
    //------------------------------------ Hent Update() -------------------------------
    //------------------------------------ Update() ------------------------------------
    //------------------------------------ Delete() ------------------------------------

}
