package com.example.estimationtool.controller;

import com.example.estimationtool.model.timeEntry.TimeEntry;
import com.example.estimationtool.service.TimeEntryService;
import com.example.estimationtool.toolbox.dto.UserViewDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

        UserViewDTO currentUser = getCurrentUser(session);

        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Du skal være logget ind for at kunne tilgå tidsregistrering.");
            return "redirect:/login";
        }

        model.addAttribute("time", new TimeEntry());

        return "timeentry/create-timeentry";
    }
    //------------------------------------ Hent Create() -------------------------------

    @PostMapping("/create")
    public String createTimeEntry(@ModelAttribute("time") TimeEntry timeEntry,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {

        UserViewDTO currentUser = getCurrentUser(session);

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


    //------------------------------------ Update() ------------------------------------

    //------------------------------------ Hent Update() -------------------------------

    //------------------------------------ Delete() ------------------------------------

}
