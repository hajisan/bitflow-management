package com.example.estimationtool.user;

import com.example.estimationtool.dto.LoginDTO;
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
@RequestMapping("users") //Base-URL for alle endpoints i UserController
public class UserController {

    private final UserService userService;

    // Dependency Injection af UserService i constructoren
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String getLogin() {
        return "index";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "index";
    }

    @PostMapping("/login")
    public String postLogin(@RequestParam("email") String email,
                            @RequestParam("password") String password,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {

        UserViewDTO userViewDTO = userService.login(email, password); // Kaster exception ved fejl fra @Service


        session.setAttribute("user", userViewDTO);
        session.setMaxInactiveInterval(1000);
        redirectAttributes.addFlashAttribute("success", "Du er nu logget ind.");

        return "redirect:/users/profile";
    }

    @GetMapping("/profile")
    public String getFrontPage(HttpSession session,
                               RedirectAttributes redirectAttributes,
                               Model model) {

        UserViewDTO currentUser = (UserViewDTO) session.getAttribute("user");

        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Du skal være logget ind for at tilgå forsiden.");
            return "redirect:/login";
        }

        model.addAttribute("user", currentUser); // valgfrit – Thymeleaf kan også hente fra session direkte
        return "front-page";
    }


    //--------------------------------- Hent Create() ----------------------------------

    @GetMapping("/create")
    public String showCreateUser(HttpSession session,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {

        User currentUser = (User) session.getAttribute("currentUser");

        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at oprette en ny bruger.");
            return "redirect:/login";
        }

        model.addAttribute("user", new UserRegistrationDTO());
        return "user/create-user"; //Thymeleaf-skabelon
    }


    //------------------------------------ Create() ------------------------------------

    @PostMapping("/create")
    public String createUser(@ModelAttribute("user") UserRegistrationDTO userDTO,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {

        User currentUser = (User) session.getAttribute("currentUser");

        // Tjekker om brugeren er logget ind
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at oprette en bruger.");
            return "redirect:/login";
        }

        userService.createUser(currentUser, userDTO);

        redirectAttributes.addFlashAttribute("succes", "Bruger blev oprettet"); //Viser succesbesked EFTER redirect

        return "redirect:/users"; //SKAL MÅSKE REDIRECTE TIL ADMINOVERSIGT?

    }



    //------------------------------------ Read() --------------------------------------

    @GetMapping("")
    public String showAllUsers(HttpSession session,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        User currentUser = (User) session.getAttribute("currentUser");

        // Tjekker om brugeren er logget ind
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at se brugeroplysninger.");
            return "redirect:/login";
        }
        List<UserViewDTO> userViewDTOList = userService.readAll();
        model.addAttribute("users", userViewDTOList);
        return "user/user-list";
    }

    @GetMapping("/{id}")
    public String showUser(@PathVariable int id,
                           HttpSession session,
                           Model model,
                           RedirectAttributes redirectAttributes) {

        User currentUser = (User) session.getAttribute("currentUser");

        // Tjekker om brugeren er logget ind
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at se brugeroplysninger.");
            return "redirect:/login";
        }

        UserViewDTO userViewDTO = userService.readById(id);
        model.addAttribute("user", userViewDTO);
        return "user/user-details";

    }

    //------------------------------------ Hent Update() -------------------------------

    @GetMapping("/edit/{id}")
    public String showEditUser(@PathVariable int id,
                               HttpSession session,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        User currentUser = (User) session.getAttribute("currentUser");

        // Tjekker om bruger er logget ind
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at opdatere bruger.");
            return "redirect:/login";
        }

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

    @PostMapping("/edit")
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

    // DUMMY tester

    @GetMapping("/test")
    @ResponseBody
    public String testPublic() {
        return "OK - no auth required";
    }



    //------------------------------------ Delete() ------------------------------------



}
