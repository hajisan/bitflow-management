package com.example.estimationtool.dto;

import com.example.estimationtool.model.Project;

import java.util.List;

public record UserWithProjectDTO(UserViewDTO user, List<Project> projectList) {
}
