package com.example.estimationtool.toolbox.exceptionHandler;

import javax.management.RuntimeErrorException;

/*
Skriv noget om hvad denne klasse gør og bruges til
 */

public class UserFriendlyException extends RuntimeException {

    public UserFriendlyException(String message) {
        super(message);
    }

}
