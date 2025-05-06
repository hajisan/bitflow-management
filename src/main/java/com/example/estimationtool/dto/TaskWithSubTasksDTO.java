package com.example.estimationtool.dto;

import com.example.estimationtool.model.SubTask;
import com.example.estimationtool.model.Task;
import java.util.List;

public record TaskWithSubTasksDTO(Task task, List<SubTask> subTaskList) {
}
