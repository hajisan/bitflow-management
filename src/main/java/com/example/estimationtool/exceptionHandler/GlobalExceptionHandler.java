package com.example.estimationtool.exceptionHandler;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


// ----------

@ControllerAdvice // Håndterer ALLE fejl i Controllers
public class GlobalExceptionHandler {

    // Fanger og håndterer alle runtime-fejl inkl. SecurityException (adgangskontrol)
    @ExceptionHandler(RuntimeException.class)
    public String handleRuntime(RuntimeException e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "error-page"; // Thymeleaf-skabelon der viser fejl
    }
}
