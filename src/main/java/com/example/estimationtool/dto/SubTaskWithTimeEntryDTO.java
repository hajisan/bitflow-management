package com.example.estimationtool.dto;

import com.example.estimationtool.subTask.SubTask;
import com.example.estimationtool.timeEntry.TimeEntry;
import java.util.List;

public record SubTaskWithTimeEntryDTO(SubTask subTask, List<TimeEntry> timeEntries) {
}
