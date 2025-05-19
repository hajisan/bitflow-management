package com.example.estimationtool.toolbox.dto;

import com.example.estimationtool.model.Task;
import com.example.estimationtool.model.TimeEntry;
import java.util.List;

public record TaskWithTimeEntriesDTO(Task task, List<TimeEntry> timeEntries) {
}
