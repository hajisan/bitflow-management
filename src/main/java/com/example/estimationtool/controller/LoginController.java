package com.example.estimationtool.controller;

import com.example.estimationtool.dto.UserViewDTO;
import com.example.estimationtool.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    private final UserService userService;

    // Dependency Injection af UserService i constructoren
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String getLogin() {
        return "index";
    }

    @PostMapping("/login")
    public String postLogin(@RequestParam("email") String email,
                            @RequestParam("password") String password,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {

        UserViewDTO currentUser = userService.login(email, password); // Kaster exception ved fejl fra @Service


        session.setAttribute("currentUser", currentUser);
        session.setMaxInactiveInterval(1000);
        redirectAttributes.addFlashAttribute("success", "Du er nu logget ind.");

        return "redirect:/users/profile";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "index";
    }

}
