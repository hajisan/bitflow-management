package com.example.estimationtool.toolbox.timeCalc;

import com.example.estimationtool.model.timeEntry.TimeEntry;

import java.time.LocalDate;
import java.util.List;

public abstract class TimeCalculator {

    public static int calculateTimeSpent(List<TimeEntry> entries) {
        return 0;
    }

    public static int calculateHoursPerDay(int remainingHours, LocalDate deadline) {
        return 0;
    }
}