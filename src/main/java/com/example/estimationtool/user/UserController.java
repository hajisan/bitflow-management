package com.example.estimationtool.user;

import com.example.estimationtool.dto.UserRegistrationDTO;
import com.example.estimationtool.dto.UserViewDTO;
import com.example.estimationtool.enums.Role;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

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

        return "redirect:/user-list"; //SKAL MÅSKE REDIRECTE TIL ADMINOVERSIGT?

    }



    //------------------------------------ Read() --------------------------------------

    @GetMapping("/user-list")
    public String showAllUsers(Model model) {
        List<UserViewDTO> userViewDTOList = userService.readAll();
        model.addAttribute("users", userViewDTOList);
        return "user/user-list";
    }


    //------------------------------------ Hent Update() -------------------------------

    //------------------------------------ Update() ------------------------------------

    //------------------------------------ Delete() ------------------------------------





    // DUMMY

    @ModelAttribute("currentUser")
    public User dummyCurrentUser(HttpSession session) {
        User dummyAdmin = new User(
                1,
                "Admin",
                "Test",
                "admin@example.com",
                "hashed", // hashet password er irrelevant her
                Role.ADMIN
        );

        session.setAttribute("currentUser", dummyAdmin);
        return dummyAdmin;
    }

}
