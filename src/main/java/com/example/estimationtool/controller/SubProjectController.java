package com.example.estimationtool.controller;

import com.example.estimationtool.model.SubProject;
import com.example.estimationtool.model.enums.Role;
import com.example.estimationtool.service.SubProjectService;
import com.example.estimationtool.toolbox.dto.UserViewDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    @GetMapping("/create")
    public String getCreateSubProject(HttpSession session,
                                      Model model,
                                      RedirectAttributes redirectAttributes) {
        UserViewDTO currentUser = getCurrentUser(session);
        if (currentUser == null) return "redirect:/login";

        Role role = currentUser.getRole();
        if (role != Role.ADMIN && role != Role.PROJECT_MANAGER) {
            redirectAttributes.addFlashAttribute("error", "Kun Admin og Projektleder kan oprette et subprojekt.");
            return "redirect:/error-page";
        }

        model.addAttribute("subproject", new SubProject());
        return "subproject/create-subproject";
    }

    @PostMapping("/create")
    public String postCreateSubProject() {


        return "";
    }
}
