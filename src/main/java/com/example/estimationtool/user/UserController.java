package com.example.estimationtool.user;

import com.example.estimationtool.dto.UserRegistrationDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users") //Base-URL for alle endpoints i UserController
public class UserController {

    private final UserService userService;

    // Dependency Injection af UserService i constructoren
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ExceptionHandler-annotationen fanger og håndterer vores SecurityException
    @ExceptionHandler(SecurityException.class)
    public String handleSecurityException(SecurityException e, Model model) {
        model.addAttribute("error", e.getMessage()); //
        return "user/create-user";
    }

    //--------------------------------- Hent Create() ----------------------------------

    @GetMapping("/create")
    public String showCreateUser(Model model) {
        model.addAttribute("user", new UserRegistrationDTO());
        return "user/create-user"; //Thymeleaf-skabelon
    }

    //------------------------------------ Create() ------------------------------------

    @PostMapping("/create")
    public String createUser(@ModelAttribute("user") UserRegistrationDTO userDTO,
                             @SessionAttribute("currentUser")
                             User currentUser,
                             RedirectAttributes redirectAttributes) {

        userService.createUser(currentUser, userDTO);

        redirectAttributes.addFlashAttribute("succes", "Bruger oprettet"); //Viser succesbesked EFTER redirect

        return "redirect:/users";

    }

    //------------------------------------ Read() --------------------------------------

    //------------------------------------ Hent Update() -------------------------------

    //------------------------------------ Update() ------------------------------------

    //------------------------------------ Delete() ------------------------------------
}
