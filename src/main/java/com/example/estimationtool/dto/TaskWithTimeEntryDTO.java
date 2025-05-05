package com.example.estimationtool.dto;

import com.example.estimationtool.model.Task;
import com.example.estimationtool.timeEntry.TimeEntry;
import java.util.List;

public record TaskWithTimeEntryDTO(Task task, List<TimeEntry> timeEntries) {
}
