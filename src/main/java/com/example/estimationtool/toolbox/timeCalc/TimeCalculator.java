package com.example.estimationtool.toolbox.timeCalc;

import com.example.estimationtool.model.Project;
import com.example.estimationtool.model.SubProject;
import com.example.estimationtool.model.SubTask;
import com.example.estimationtool.model.Task;
import com.example.estimationtool.model.timeEntry.TimeEntry;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;

public abstract class TimeCalculator {

    public static int calculateTimeSpent(List<TimeEntry> entries) {
        if (!entries.isEmpty()) {
            int totalTimeSpent = 0;

            for (TimeEntry timeEntry : entries) {
                totalTimeSpent += timeEntry.getHoursSpent();
            }

            return totalTimeSpent;
        } else {
            throw new NullPointerException("Listen af Time Entries er tom.");
        }
    }

    public static double calculateRemainingTime(LocalDate deadline) {
        return countWorkDays(deadline) * (37 / 5.0); // Vi antager at en arbejdsuge er 37 timer med 5 hverdage
    }

    public static double calculateHoursPerDay(int remainingHours, LocalDate deadline) {
        // until-metoden hos LocalDate returnerer longs og ikke ints, hvilket er grunden til at værdien bliver castet
        double daysUntilDeadline = countWorkDays(deadline); // Se metoden countWorkDays
        if (daysUntilDeadline > 0) { // Sørger for at vi er før deadlinen, samtidig med at undgå at dividere med 0
            return (double) remainingHours / daysUntilDeadline; // Returnerer timer pr. dag til deadline
        } else if (daysUntilDeadline == 0) return remainingHours; // Hvis daysUntilDeadline er 0, så er vi på
        return 0; // TODO Skal vi bruge en exception her i stedet?
    }

    private static double countWorkDays(LocalDate deadline) {
        double workingDays = 0.0; // Vi starter med 0 hverdage og inkrementerer hver gang vi møder en hverdag i while-loopet
        LocalDate dateToCheck = LocalDate.now(); // Den dato vi tjekker - vi starter med at tjekke i dag

        while (dateToCheck.isBefore(deadline)) {        // Så længe den dato vi tjekker er før deadlinen kører loopet
            DayOfWeek day = dateToCheck.getDayOfWeek(); // Vi henter den dag på ugen vi har på den dato vi vil tjekke
            if (day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY) { // Hvis det ikke er lørdag og ikke er søndag
                workingDays++;                                          // tilføjer vi en hverdag til vores variabel
            }
            dateToCheck = dateToCheck.plusDays(1); // Vi går videre til næste dato der skal tjekkes ved at tilføje en dag til datoen
        }

        return workingDays;
    }

    public static double calculateHoursPerDayForProject(Project project) {
        double remainingHours = calculateRemainingTime(project.getDeadline());
        LocalDate deadline = project.getDeadline();
        return calculateHoursPerDay((int) remainingHours, deadline);
    }

    public static double calculateHoursPerDayForSubProject(SubProject subProject) {
        double remainingHours = calculateRemainingTime(subProject.getDeadline());
        LocalDate deadline = subProject.getDeadline();
        return calculateHoursPerDay((int) remainingHours, deadline);
    }

    public static double calculateHoursPerDayForTask(Task task) {
        double remainingHours = calculateRemainingTime(task.getDeadline());
        LocalDate deadline = task.getDeadline();
        return calculateHoursPerDay((int) remainingHours, deadline);
    }

    public static double calculateHoursPerDayForSubTask(SubTask subTask) {
        double remainingHours = calculateRemainingTime(subTask.getDeadline());
        LocalDate deadline = subTask.getDeadline();
        return calculateHoursPerDay((int) remainingHours, deadline);
    }
}