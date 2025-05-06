package com.example.estimationtool.dto;

import com.example.estimationtool.model.SubProject;
import com.example.estimationtool.model.Task;

import java.util.List;

public record SubProjectWithTasksDTO(SubProject subProject, List<Task> taskList) {
}
