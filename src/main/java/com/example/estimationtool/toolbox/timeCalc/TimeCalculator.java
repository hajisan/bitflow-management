package com.example.estimationtool.toolbox.timeCalc;

import com.example.estimationtool.model.Project;
import com.example.estimationtool.model.SubProject;
import com.example.estimationtool.model.SubTask;
import com.example.estimationtool.model.Task;
import com.example.estimationtool.model.timeEntry.TimeEntry;

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

    public static double calculateHoursPerDay(int remainingHours, LocalDate deadline) {
        // until-metoden hos LocalDate returnerer longs og ikke doubles, hvilket er grunden til at værdien bliver castet
        double daysUntilDeadline = (double) LocalDate.now().until(deadline, ChronoUnit.DAYS);
        if (daysUntilDeadline > 0) { // Sørger for at vi er før deadlinen, samtidig med at undgå at dividere med 0
            return remainingHours / daysUntilDeadline;
        } else if (daysUntilDeadline == 0) return remainingHours; // Hvis daysUntilDeadline er 0, så er vi på DL-dagen
        return 0; // TODO Skal vi bruge en exception her i stedet?
    }

    public static double calculateHoursPerDayForProject(Project project) {
        int remainingHours = project.getEstimatedTime() - project.getTimeSpent();
        LocalDate deadline = project.getDeadline();
        return calculateHoursPerDay(remainingHours, deadline);
    }

    public static double calculateHoursPerDayForSubProject(SubProject subProject) {
        int remainingHours = subProject.getEstimatedTime() - subProject.getTimeSpent();
        LocalDate deadline = subProject.getDeadline();
        return calculateHoursPerDay(remainingHours, deadline);
    }

    public static double calculateHoursPerDayForTask(Task task) {
        int remainingHours = task.getEstimatedTime() - task.getTimeSpent();
        LocalDate deadline = task.getDeadline();
        return calculateHoursPerDay(remainingHours, deadline);
    }

    public static double calculateHoursPerDayForSubTask(SubTask subTask) {
        int remainingHours = subTask.getEstimatedTime() - subTask.getTimeSpent();
        LocalDate deadline = subTask.getDeadline();
        return calculateHoursPerDay(remainingHours, deadline);
    }
}