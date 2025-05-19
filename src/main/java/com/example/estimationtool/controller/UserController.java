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
@RequestMapping("/users") //Base-URL for alle endpoints i UserController
public class UserController {

    private final UserService userService;

    // Dependency Injection af UserService i konstruktøren
    public UserController(UserService userService) {
        this.userService = userService;
    }


    private UserViewDTO getCurrentUser(HttpSession session) {
        return (UserViewDTO) session.getAttribute("currentUser");
    }

    //--------------------------------- Efter login --------------------------------

    // TODO - DONE
    @GetMapping("/profile")
    public String getFrontPage(HttpSession session,
                               RedirectAttributes redirectAttributes,
                               Model model) {

        // Henter og sætter session for Thymeleaf
        UserViewDTO currentUser = getCurrentUser(session);
        session.setAttribute("currentUser", currentUser);

        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Du skal være logget ind for at tilgå forsiden.");
            return "redirect:/login";
        }

        return "user/front-page";
    }



    //--------------------------------- Hent Create() ----------------------------------

    // TODO - DONE
    @GetMapping("/create")
    public String showCreateUser(HttpSession session,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {

        // Henter og sætter session for Thymeleaf
        UserViewDTO currentUser = getCurrentUser(session);
        session.setAttribute("currentUser", currentUser);

        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at oprette en ny bruger.");
            return "redirect:/login";
        }

        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO();
        model.addAttribute("user", userRegistrationDTO);

        return "user/create-user";
    }


    //------------------------------------ Create() ------------------------------------

    // TODO - DONE
    @PostMapping("/create")
    public String createUser(@ModelAttribute("user") UserRegistrationDTO userDTO,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {

        // Henter og sætter session for Thymeleaf
        UserViewDTO currentUser = getCurrentUser(session);
        session.setAttribute("currentUser", currentUser);

        // Tjekker om brugeren er logget ind
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at oprette en bruger.");
            return "redirect:/login";
        }

        userService.createUser(currentUser, userDTO);

        redirectAttributes.addFlashAttribute("success", "Bruger blev oprettet.");

        return "redirect:/users/list";

    }

    //------------------------------------ Read() --------------------------------------

    // TODO - DONE
    @GetMapping("/list")
    public String showAllUsers(HttpSession session,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        // Henter og sætter session for Thymeleaf
        UserViewDTO currentUser = getCurrentUser(session);
        session.setAttribute("currentUser", currentUser);

        // Tjekker om brugeren er logget ind
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at se brugeroplysninger.");
            return "redirect:/login";
        }

        List<UserViewDTO> userViewDTOList = userService.readAll();
        model.addAttribute("users", userViewDTOList);

        return "user/user-list";
    }

    // TODO - DONE

    @GetMapping("/{userId}")
    public String showUser(@PathVariable int userId,
                           HttpSession session,
                           Model model,
                           RedirectAttributes redirectAttributes) {

        // Henter og sætter session for Thymeleaf
        UserViewDTO currentUser = getCurrentUser(session);
        session.setAttribute("currentUser", currentUser);

        // Tjekker om brugeren er logget ind
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at se brugeroplysninger.");
            return "redirect:/login";
        }

        UserViewDTO userViewDTO = userService.readById(userId);
        model.addAttribute("user", userViewDTO);

        return "user/user-detail";

    }

    //------------------------------------ Hent Update() -------------------------------

    // TODO - DONE
    @GetMapping("/edit/{userId}")
    public String showEditUser(@PathVariable int userId,
                               HttpSession session,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        // Henter og sætter session for Thymeleaf
        UserViewDTO currentUser = getCurrentUser(session);
        session.setAttribute("currentUser", currentUser);

        // Tjekker om bruger er logget ind
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at opdatere bruger.");
            return "redirect:/login";
        }


        // Kalder metode, der konverterer UserViewDTO til en UserUpdateDTO
        UserUpdateDTO userUpdateDTO = userService.getUserUpdateDTOById(userId);
        model.addAttribute("userUpdateDTO", userUpdateDTO);

        return "user/edit-user";

    }

    //------------------------------------ Update() ------------------------------------

    // TODO - DONE
    @PostMapping("/update")
    public String updateUser(@ModelAttribute("userUpdateDTO") UserUpdateDTO userUpdateDTO,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {

        // Henter og sætter session for Thymeleaf
        UserViewDTO currentUser = getCurrentUser(session);
        session.setAttribute("currentUser", currentUser);

        // Tjekker om bruger er logget ind
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at opdatere bruger.");
            return "redirect:/login";
        }

        userService.updateUser(userUpdateDTO, currentUser);

        redirectAttributes.addFlashAttribute("success", "Brugeren blev opdateret.");

        return "redirect:/users/" + userUpdateDTO.getUserId(); // Redirect til user-detail.html
    }


    //------------------------------------ Delete() ------------------------------------


    // TODO - DONE
    @PostMapping("/delete/{userId}")
    public String deleteUser(@PathVariable int userId,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {

        // Henter og sætter session for Thymeleaf
        UserViewDTO currentUser = getCurrentUser(session);
        session.setAttribute("currentUser", currentUser);

        // Tjekker om bruger er logget ind
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Du skal være logget ind for at kunne slette brugeren.");
            return "redirect:/login";
        }

        userService.deleteById(userId, currentUser);

        redirectAttributes.addFlashAttribute("success", "Brugeren blev slettet.");

        return "redirect:/users/list";

    }
    //---------------------------------- DTO read() ------------------------------------

    // TODO - DONE

    // -------------------- Viser brugerens tilknyttede projekter ----------------------

    @GetMapping("/{userId}/projects")
    public String showUserWithProjects(@PathVariable int userId,
                                       HttpSession session,
                                       Model model,
                                       RedirectAttributes redirectAttributes) {

        // Henter og sætter session for Thymeleaf
        UserViewDTO currentUser = getCurrentUser(session);
        session.setAttribute("currentUser", currentUser);

        // Tjekker om bruger er logget ind
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Du skal være logget ind for at kunne se brugerens projekter.");
            return "redirect:/login";
        }

        UserWithProjectsDTO userWithProjectsDTO = userService.readAllProjectsByUserId(userId);
        model.addAttribute("userWithProjects", userWithProjectsDTO);

        return "user/user-with-projects";
    }

    // -------------------- Viser brugerens tilknyttede subprojekter --------------------

    // TODO - DONE
    @GetMapping("/{userId}/subprojects")
    public String showUserWithSubProjects(@PathVariable int userId,
                                          HttpSession session,
                                          Model model,
                                          RedirectAttributes redirectAttributes) {

        // Henter og sætter session for Thymeleaf
        UserViewDTO currentUser = getCurrentUser(session);
        session.setAttribute("currentUser", currentUser);

        // Tjekker om bruger er logget ind
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Du skal være logget ind for at kunne se en brugers subprojekter.");
            return "redirect:/login";
        }

        UserWithSubProjectsDTO userWithSubProjectsDTO = userService.readAllSubProjectsByUserId(userId);
        model.addAttribute("userWithSubProjects", userWithSubProjectsDTO);

        return "user/user-with-subprojects";
    }

    // -------------------- Viser brugerens tilknyttede tasks --------------------

    // TODO - DONE
    @GetMapping("/{userId}/tasks")
    public String showUserWithTasks(@PathVariable int userId,
                                    HttpSession session,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {

        // Henter og sætter session for Thymeleaf
        UserViewDTO currentUser = getCurrentUser(session);
        session.setAttribute("currentUser", currentUser);

        // Tjekker om bruger er logget ind
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Du skal være logget ind for at kunne se en brugers tasks.");
            return "redirect:/login";
        }

        UserWithTasksDTO userWithTasksDTO = userService.readAllTasksByUserId(userId);
        model.addAttribute("userWithTasks", userWithTasksDTO);

        return "user/user-with-tasks";

    }

    // ------------------ Viser brugerens tilknyttede subtasks --------------------

    // TODO - DONE
    @GetMapping("/{userId}/subtasks")
    public String showUserWithSubTasks(@PathVariable int userId,
                                       HttpSession session,
                                       Model model,
                                       RedirectAttributes redirectAttributes) {

        // Henter og sætter session for Thymeleaf
        UserViewDTO currentUser = getCurrentUser(session);
        session.setAttribute("currentUser", currentUser);

        // Tjekker om bruger er logget ind
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Du skal være logget ind for at kunne se en brugers subtasks.");
            return "redirect:/login";
        }

        // Henter DTO: bruger + subopgaver
        UserWithSubTasksDTO userWithSubTasksDTO = userService.readAllSubTasksByUserId(userId);
        // Lægger DTO på modellen
        model.addAttribute("userWithSubTasks", userWithSubTasksDTO);

        return "user/user-with-subtasks";
    }

    @GetMapping("/{userId}/timeentries")
    public String showUserWithTimeEntries(@PathVariable int userId,
                                    HttpSession session,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {

        // Henter og sætter session for Thymeleaf
        UserViewDTO currentUser = getCurrentUser(session);
        session.setAttribute("currentUser", currentUser);

        // Tjekker om bruger er logget ind
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Du skal være logget ind for at kunne se en brugers tasks.");
            return "redirect:/login";
        }

        // Henter DTO: bruger + time entries
        UserWithTimeEntriesDTO userWithTimeEntriesDTO = userService.readAllTimeEntriesByUserId(userId);
        // Lægger DTO på modellen
        model.addAttribute("userWithTimeEntries", userWithTimeEntriesDTO);

        return "user/user-with-timeentries";

    }

}
