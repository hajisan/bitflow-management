package com.example.estimationtool.dto;

import com.example.estimationtool.task.Task;
import java.util.List;

public record UserWithTaskDTO(UserViewDTO user, List<Task> taskList) {
}
