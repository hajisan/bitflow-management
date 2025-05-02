package com.example.estimationtool.dto;

import com.example.estimationtool.subTask.SubTask;
import com.example.estimationtool.task.Task;
import java.util.List;

public record TaskWithSubTasksDTO(Task task, List<SubTask> subTaskList) {
}
