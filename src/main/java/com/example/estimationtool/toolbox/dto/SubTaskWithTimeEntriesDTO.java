package com.example.estimationtool.toolbox.dto;

import com.example.estimationtool.model.SubTask;
import com.example.estimationtool.model.timeEntry.TimeEntry;
import com.example.estimationtool.toolbox.timeCalc.TimeCalculator;

import java.util.List;

public record SubTaskWithTimeEntriesDTO(SubTask subTask, List<TimeEntry> timeEntries) {

    // Skal måske slet ikke bruges
    public int getTimeSpent() {
        return TimeCalculator.calculateTimeSpent(timeEntries);
    }
}
