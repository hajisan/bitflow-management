package com.example.estimationtool.toolbox.dto;

import com.example.estimationtool.model.SubTask;
import com.example.estimationtool.model.timeEntry.TimeEntry;
import java.util.List;

public record SubTaskWithTimeEntryDTO(SubTask subTask, List<TimeEntry> timeEntries) {
}
