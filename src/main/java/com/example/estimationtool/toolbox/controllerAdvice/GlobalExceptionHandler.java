package com.example.estimationtool.toolbox.controllerAdvice;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/*
 En brugerdefineret exception beregnet til at give brugeren en venlig fejlbesked.
 Bruges i service-laget, hvor fejl skal meldes videre til frontend uden stacktrace.
 */

@ControllerAdvice // Håndterer ALLE fejl i Controllers
public class GlobalExceptionHandler {

    // Fanger og håndterer alle runtime-fejl inkl. SecurityException (adgangskontrol)
    @ExceptionHandler(RuntimeException.class)
    public String handleRuntime(RuntimeException e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "error-page"; // Thymeleaf-skabelon der viser fejl
    }

    @ExceptionHandler(UserFriendlyException.class)
    public String handleFriendly(UserFriendlyException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return "redirect:" + e.getRedirectPath();
    }

}
