package com.example.estimationtool.toolbox.dto;

import com.example.estimationtool.model.Task;
import java.util.List;

public record UserWithTasksDTO(UserViewDTO user, List<Task> taskList) {
}
