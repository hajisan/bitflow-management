package com.example.estimationtool.toolbox.dto;

import com.example.estimationtool.model.TimeEntry;
import java.util.List;

public record UserWithTimeEntriesDTO(UserViewDTO user, List<TimeEntry> timeEntries) {
}
