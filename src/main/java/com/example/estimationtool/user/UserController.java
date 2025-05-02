package com.example.estimationtool.user;

import com.example.estimationtool.dto.UserRegistrationDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users") //Base-URL for alle endpoints i UserController
public class UserController {

    private final UserService userService;

    // Dependency Injection af UserService i constructoren
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //--------------------------------- Hent Create() ----------------------------------

    @GetMapping("create")
    public String showCreateUser(Model model) {
        model.addAttribute("user", new UserRegistrationDTO());
        return "user/create-user"; //Thymeleaf-skabelon
    }

    //------------------------------------ Create() ------------------------------------

    @PostMapping("create")
    public String createUser()

    //------------------------------------ Read() --------------------------------------

    //------------------------------------ Hent Update() -------------------------------

    //------------------------------------ Update() ------------------------------------

    //------------------------------------ Delete() ------------------------------------
}
