package com.example.estimationtool.dto;
import com.example.estimationtool.subTask.SubTask;

import java.util.List;

public record UserWithSubTaskDTO(UserViewDTO user, List<SubTask> subTaskList) {
}
