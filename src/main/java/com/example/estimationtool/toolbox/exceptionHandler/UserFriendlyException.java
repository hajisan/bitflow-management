package com.example.estimationtool.toolbox.exceptionHandler;

/*
 * Denne klasse repræsenterer en specialdesignet exception, der bruges til at give brugervenlige fejlbeskeder.
 * Den håndterer fejl på en måde, der undgår at vise tekniske detaljer, som f.eks. stack traces, i frontend.
 * Exceptions inkluderer en fejlbesked til brugeren og en redirect path, der guider dem til den rette side efter fejlen.
 */

public class UserFriendlyException extends RuntimeException {
    private final String redirectPath;

    public UserFriendlyException(String message, String redirectPath) {
        super(message);
        this.redirectPath = redirectPath;
    }

    public String getRedirectPath() {
        return redirectPath;
    }

}
