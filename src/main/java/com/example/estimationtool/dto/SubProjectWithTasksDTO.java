package com.example.estimationtool.dto;

import com.example.estimationtool.subProject.SubProject;
import com.example.estimationtool.task.Task;

import java.util.List;

public record SubProjectWithTasksDTO(SubProject subProject, List<Task> taskList) {
}
