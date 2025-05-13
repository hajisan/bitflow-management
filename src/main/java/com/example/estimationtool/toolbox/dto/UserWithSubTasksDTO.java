package com.example.estimationtool.toolbox.dto;
import com.example.estimationtool.model.SubTask;

import java.util.List;

public record UserWithSubTasksDTO(UserViewDTO user, List<SubTask> subTaskList) {
}
