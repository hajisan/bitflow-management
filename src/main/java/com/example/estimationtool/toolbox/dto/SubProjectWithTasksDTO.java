package com.example.estimationtool.toolbox.dto;

import com.example.estimationtool.model.SubProject;
import com.example.estimationtool.model.Task;

import java.util.List;

public record SubProjectWithTasksDTO(SubProject subProject, List<Task> taskList) {
}
