package com.example.estimationtool.controller;

import com.example.estimationtool.toolbox.dto.*;
import com.example.estimationtool.model.enums.Role;
import com.example.estimationtool.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.parameters.P;
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


    private UserViewDTO getCurrentUser(HttpSession session) {
        return (UserViewDTO) session.getAttribute("currentUser");
    }

    //--------------------------------- Efter login --------------------------------
    
    @GetMapping("/profile")
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



    //--------------------------------- Hent Create() ----------------------------------

    @GetMapping("/create")
    public String showCreateUser(HttpSession session,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {

        UserViewDTO currentUser = getCurrentUser(session);

        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at oprette en ny bruger.");
            return "redirect:/login";
        }

        model.addAttribute("user", new UserRegistrationDTO());
        return "user/create-user";
    }


    //------------------------------------ Create() ------------------------------------

    @PostMapping("/create")
    public String createUser(@ModelAttribute("user") UserRegistrationDTO userDTO,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {

        UserViewDTO currentUser = getCurrentUser(session);

        // Tjekker om brugeren er logget ind
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at oprette en bruger.");
            return "redirect:/login";
        }

        userService.createUser(currentUser, userDTO);

        redirectAttributes.addFlashAttribute("success", "Bruger blev oprettet"); //Viser succesbesked EFTER redirect

        return "redirect:/users/users"; //SKAL MÅSKE REDIRECTE TIL ADMINOVERSIGT?

    }

    //------------------------------------ Read() --------------------------------------

    @GetMapping("users")
    public String showAllUsers(HttpSession session,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        UserViewDTO currentUser = getCurrentUser(session);

        // Tjekker om brugeren er logget ind
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at se brugeroplysninger.");
            return "redirect:/login";
        }

        List<UserViewDTO> userViewDTOList = userService.readAll();
        model.addAttribute("users", userViewDTOList);
        model.addAttribute("isAdmin", currentUser.getRole() == Role.ADMIN);
        return "user/user-list";
    }


    @GetMapping("/{id}")
    public String showUser(@PathVariable int id,
                           HttpSession session,
                           Model model,
                           RedirectAttributes redirectAttributes) {

        UserViewDTO currentUser = getCurrentUser(session);

        // Tjekker om brugeren er logget ind
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at se brugeroplysninger.");
            return "redirect:/login";
        }

        UserViewDTO userViewDTO = userService.readById(id);

        model.addAttribute("user", userViewDTO);

        return "user/user-detail";

    }

    //------------------------------------ Hent Update() -------------------------------

    @GetMapping("/edit/{id}")
    public String showEditUser(@PathVariable int id,
                               HttpSession session,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        UserViewDTO currentUser = getCurrentUser(session);

        // Tjekker om bruger er logget ind
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at opdatere bruger.");
            return "redirect:/login";
        }

        // @TODO - Ryk nedenstående til service-klassen. Vi skal ikke mappe DTO'er her

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

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("userUpdateDTO") UserUpdateDTO userUpdateDTO,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {

        UserViewDTO currentUser = getCurrentUser(session);

        // Tjekker om bruger er logget ind
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at opdatere bruger.");
            return "redirect:/login";
        }

        userService.updateUser(userUpdateDTO, currentUser);

        // Tilføj succesbesked som flash-attribut (vises efter redirect)
        redirectAttributes.addFlashAttribute("success", "Brugeren blev opdateret.");

        return "redirect:/users/" + userUpdateDTO.getUserId(); // Redirect til user-detail
    }


    //------------------------------------ Delete() ------------------------------------

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable int id,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {


        // Tjekker om bruger er logget ind
        UserViewDTO currentUser = getCurrentUser(session);
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Du skal være logget ind for at kunne slette brugeren.");
            return "redirect:/login";
        }

        userService.deleteById(id, currentUser);

        // @TODO - KUN ADMIN må slette en bruger. Der er roleCheck i Service ->
        //  @TODO - Måske skal den sendes med her?

        redirectAttributes.addFlashAttribute("success", "Brugeren blev slettet.");

        return "redirect:/users/users";

    }
    //---------------------------------- DTO read() ------------------------------------


    // -------------------- Viser brugerens tilknyttede projekter ----------------------

    @GetMapping("/{id}/projects")
    public String showUserWithProjects(@PathVariable int id,
                                       HttpSession session,
                                       Model model,
                                       RedirectAttributes redirectAttributes) {

        // Tjekker om bruger er logget ind
        UserViewDTO currentUser = getCurrentUser(session);
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Du skal være logget ind for at kunne slette brugeren.");
            return "redirect:/login";
        }

        UserWithProjectsDTO userWithProjectsDTO = userService.readAllProjectsByUserId(id);

        model.addAttribute("userWithProjects", userWithProjectsDTO);

        return "user/user-with-projects";
    }

    // -------------------- Viser brugerens tilknyttede subprojekter --------------------

    @GetMapping("/{id}/subprojects")
    public String showUserWithSubProjects(@PathVariable int id,
                                          HttpSession session,
                                          Model model,
                                          RedirectAttributes redirectAttributes) {

        // Tjekker om bruger er logget ind
        UserViewDTO currentUser = getCurrentUser(session);
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Du skal være logget ind for at kunne se en brugers subprojekter.");
            return "redirect:/login";
        }

        UserWithSubProjectsDTO userWithSubProjectsDTO = userService.readAllSubProjectsByUserId(id);

        model.addAttribute("userWithSubProjects", userWithSubProjectsDTO);
        return "user/user-with-subprojects";
    }

    // -------------------- Viser brugerens tilknyttede tasks --------------------

    @GetMapping("/{id}/tasks")
    public String showUserWithTasks(@PathVariable int id,
                                    HttpSession session,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {

        // Tjekker om bruger er logget ind
        UserViewDTO currentUser = getCurrentUser(session);
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Du skal være logget ind for at kunne se en brugers opgaver.");
            return "redirect:/login";
        }

        UserWithTasksDTO userWithTasksDTO = userService.readAllTasksByUserId(id);

        model.addAttribute("userWithTasks", userWithTasksDTO);

        return "user/user-with-tasks";

    }

    // ------------------ Viser brugerens tilknyttede subtasks --------------------

    @GetMapping("/{id}/subtasks")
    public String showUserWithSubTasks(@PathVariable int id,
                                       HttpSession session,
                                       Model model,
                                       RedirectAttributes redirectAttributes) {

        // Tjekker om bruger er logget ind
        UserViewDTO currentUser = getCurrentUser(session);
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Du skal være logget ind for at kunne se en brugers subopgaver.");
            return "redirect:/login";
        }

        // Henter DTO: bruger + subopgaver
        UserWithSubTasksDTO userWithSubTasksDTO = userService.readAllSubTasksByUserId(id);

        // Lægger DTO på modellen
        model.addAttribute("userWithSubTasks", userWithSubTasksDTO);

        return "user/user-with-subtasks";
    }









}
