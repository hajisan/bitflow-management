package com.example.estimationtool.controller;

import com.example.estimationtool.model.TimeEntry;
import com.example.estimationtool.service.TimeEntryService;
import com.example.estimationtool.toolbox.dto.UserViewDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("timeentries")
public class TimeEntryController {

    private final TimeEntryService timeEntryService;

    public TimeEntryController(TimeEntryService timeEntryService) {
        this.timeEntryService = timeEntryService;
    }

    private UserViewDTO getCurrentUser(HttpSession session) {
        return (UserViewDTO) session.getAttribute("currentUser");
    }

    //------------------------------------ Create() ------------------------------------

    @GetMapping("/create")
    public String showCreateTimeEntry(Model model,
                                      HttpSession session,
                                      RedirectAttributes redirectAttributes) {

        // Henter og sætter session for Thymeleaf
        UserViewDTO currentUser = getCurrentUser(session);
        session.setAttribute("currentUser", currentUser);

        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Du skal være logget ind for at kunne oprette en tidsregistrering.");
            return "redirect:/login";
        }

        model.addAttribute("timeentry", new TimeEntry());

        return "timeentry/create-timeentry";
    }
    //------------------------------------ Hent Create() -------------------------------

    @PostMapping("/create")
    public String createTimeEntry(@ModelAttribute("timeentry") TimeEntry timeEntry,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {

        // Henter og sætter session for Thymeleaf
        UserViewDTO currentUser = getCurrentUser(session);
        session.setAttribute("currentUser", currentUser);

        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Du skal være logget ind for at kunne oprette en tidsregistrering.");
            return "redirect:/login";
        }

        // Tjekker om brugeren er logget ind
        timeEntryService.createTimeEntry(timeEntry);

        redirectAttributes.addFlashAttribute("success", "Tidsregistrering lykkedes.");

        return "redirect:/timeentries";
    }
    //------------------------------------ Read() --------------------------------------

    @GetMapping("/list")
    public String showAllTimeEntries(Model model,
                                     HttpSession session,
                                     RedirectAttributes redirectAttributes) {

        // Henter og sætter session for Thymeleaf
        UserViewDTO currentUser = getCurrentUser(session);
        session.setAttribute("currentUser", currentUser);

        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Du skal være logget ind for at kunne se tidsregistreringer.");
            return "redirect:/login";
        }

        List<TimeEntry> timeEntryList = timeEntryService.readAll();

        model.addAttribute("timeentries", timeEntryList);
        return "timeentry/timeentry-list";
    }

    @GetMapping("/{id}")
    public String showTimeEntry(@PathVariable int id,
                                Model model,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {


        // Henter og sætter session for Thymeleaf
        UserViewDTO currentUser = getCurrentUser(session);
        session.setAttribute("currentUser", currentUser);

        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Du skal være logget ind for at kunne se en tidsregistrering.");
            return "redirect:/login";
        }

        TimeEntry timeEntry = timeEntryService.readById(id);

        model.addAttribute("timeentry", timeEntry);
        return "timeentry/timeentry-detail";
    }

    //------------------------------------ Hent Update() -------------------------------

    @GetMapping("/edit/{id}")
    public String showEditTimeEntry(@PathVariable int id,
                                    Model model,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {

        // Henter og sætter session for Thymeleaf
        UserViewDTO currentUser = getCurrentUser(session);
        session.setAttribute("currentUser", currentUser);

        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Du skal være logget ind for at kunne ændre en tidsregistrering.");
            return "redirect:/login";
        }

        TimeEntry timeEntry = timeEntryService.readById(id);

        model.addAttribute("timeentry", timeEntry);
        return "timeentry/edit-timeentry";

    }

    //------------------------------------ Update() ------------------------------------

    @PostMapping("/update")
    public String updateTimeEntry(@ModelAttribute("timeentry") TimeEntry timeEntry,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {

        // Henter og sætter session for Thymeleaf
        UserViewDTO currentUser = getCurrentUser(session);
        session.setAttribute("currentUser", currentUser);

        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Du skal være logget ind for at kunne ændre en tidsregistrering.");
            return "redirect:/login";
        }


        timeEntryService.updateTimeEntry(timeEntry);

        redirectAttributes.addFlashAttribute("success", "Ændring af tidsregistrering lykkedes.");


        return "redirect:/timeentries/" + timeEntry.getTimeId(); // Redirect til timeentry-detail.html

    }
    //------------------------------------ Delete() ------------------------------------

    @PostMapping("/delete/{id}")
    public String deleteTimeEntry(@PathVariable int id,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {

        // Henter og sætter session for Thymeleaf
        UserViewDTO currentUser = getCurrentUser(session);
        session.setAttribute("currentUser", currentUser);

        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Du skal være logget ind for at kunne slette en tidsregistrering.");
            return "redirect:/login";
        }

        timeEntryService.deleteById(id);

        redirectAttributes.addFlashAttribute("success", "Tidsregistrering blev slettet.");

        return "redirect:/timeentries";
    }




}
