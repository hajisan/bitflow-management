package com.example.estimationtool.toolbox.dto;

import com.example.estimationtool.model.Task;
import com.example.estimationtool.model.timeEntry.TimeEntry;
import java.util.List;

public record TaskWithTimeEntryDTO(Task task, List<TimeEntry> timeEntries) {
}
