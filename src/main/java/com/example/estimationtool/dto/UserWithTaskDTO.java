package com.example.estimationtool.dto;

import com.example.estimationtool.model.Task;
import java.util.List;

public record UserWithTaskDTO(UserViewDTO user, List<Task> taskList) {
}
