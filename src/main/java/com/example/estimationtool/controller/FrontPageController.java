package com.example.estimationtool.controller;

import com.example.estimationtool.toolbox.dto.UserViewDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("front-page")
public class FrontPageController {

    public FrontPageController() {
    }

    private UserViewDTO getCurrentUser(HttpSession session) {
        return (UserViewDTO) session.getAttribute("currentUser");
    }

    @GetMapping("")
    public String getFrontPage(HttpSession session,
                               RedirectAttributes redirectAttributes,
                               Model model) {

        UserViewDTO currentUser = getCurrentUser(session);

        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Du skal være logget ind for at tilgå forsiden.");
            return "redirect:/login";
        }

        model.addAttribute("user", currentUser);
        return "user/front-page";
    }
}
