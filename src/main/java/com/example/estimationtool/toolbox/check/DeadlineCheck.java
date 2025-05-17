package com.example.estimationtool.toolbox.check;

import java.time.LocalDate;

public class DeadlineCheck {

    //----- Sørger for at null-værdi for deadline-input ikke overskriver databasen -----

    public static LocalDate checkForDeadlineInput(LocalDate incomingDeadline, LocalDate existingDeadline) {
        if (incomingDeadline != null) {
            return incomingDeadline;
        } else {
            return existingDeadline;
        }
    }
}
