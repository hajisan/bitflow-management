package com.example.estimationtool.user;

import com.example.estimationtool.dto.UserRegistrationDTO;
import com.example.estimationtool.dto.UserUpdateDTO;
import com.example.estimationtool.dto.UserViewDTO;
import com.example.estimationtool.enums.Role;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping() //Base-URL for alle endpoints i UserController
public class UserController {

    private final UserService userService;

    // Dependency Injection af UserService i constructoren
    public UserController(UserService userService) {
        this.userService = userService;
    }


    //--------------------------------- Hent Create() ----------------------------------

    @GetMapping("/create")
    public String showCreateUser(@SessionAttribute("currentUser") User currentUser,
                                 Model model) {
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

        redirectAttributes.addFlashAttribute("succes", "Brugere blev oprettet"); //Viser succesbesked EFTER redirect

        return "redirect:/users"; //SKAL MÅSKE REDIRECTE TIL ADMINOVERSIGT?

    }



    //------------------------------------ Read() --------------------------------------

    @GetMapping("/users")
    public String showAllUsers(@SessionAttribute("currentUser") User currentUser,
                               Model model) {
        List<UserViewDTO> userViewDTOList = userService.readAll();
        model.addAttribute("users", userViewDTOList);
        return "user/user-list";
    }

    @GetMapping("users/{id}")
    public String showUser(@PathVariable int id,
                           @SessionAttribute("currentUser") User currentUser,
                           Model model) {
        UserViewDTO userViewDTO = userService.readById(id);
        model.addAttribute("user", userViewDTO);
        return "user/user-details";

    }

    //------------------------------------ Hent Update() -------------------------------

    @GetMapping("/users/edit/{id}")
    public String showEditUser(@PathVariable int id,
                               @SessionAttribute("currentUser") User currentUser,
                               Model model) {

        UserViewDTO userViewDTO = userService.readById(id);

        // Konverterer UserViewDTO til UserUpdateDTO — password = tomt (vises ikke)
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO(
                userViewDTO.getUserId(),
                userViewDTO.getFirstName(),
                userViewDTO.getLastName(),
                userViewDTO.getEmail(),
                "", // Tomt felt, skal indtastes igen
                userViewDTO.getRole()
        );

        model.addAttribute("userUpdateDTO", userUpdateDTO);
        model.addAttribute("isAdmin", currentUser.getRole() == Role.ADMIN); // Så admin kan se rolle-felter
        return "user/edit-user";

    }


    //------------------------------------ Update() ------------------------------------

    @PostMapping("/users/edit")
    public String updateUser(@ModelAttribute("userUpdateDTO") UserUpdateDTO userUpdateDTO,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {

        User currentUser = (User) session.getAttribute("currentUser");

        // Tjekker om bruger er logget ind
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at opdatere bruger.");
            return "redirect:/login";
        }

        userService.updateUser(userUpdateDTO, currentUser);

        // Tilføj succesbesked som flash-attribut (vises efter redirect)
        redirectAttributes.addFlashAttribute("succes", "Brugeren blev opdateret.");

        return "redirect:/users/" + userUpdateDTO.getUserId(); // Redirect til user-detail
    }





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
