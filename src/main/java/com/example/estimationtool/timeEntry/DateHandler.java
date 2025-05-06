package com.example.estimationtool.timeEntry;

import java.time.LocalDate;

public class DateHandler {

    // Statisk metode, da vi vil kunne bruge den uanset om vi har en instans af Profile eller ej
    public static LocalDate getLocalDateFromString(String date) {
        return LocalDate.of(
                Integer.parseInt(date.split("-")[0]),
                Integer.parseInt(date.split("-")[1]),
                Integer.parseInt(date.split("-")[2])
        );
    }
}
