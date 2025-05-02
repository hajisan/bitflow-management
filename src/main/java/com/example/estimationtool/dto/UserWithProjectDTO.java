package com.example.estimationtool.dto;

import com.example.estimationtool.project.Project;

import java.util.List;

public record UserWithProjectDTO(UserViewDTO userViewDTO, List<Project> projectList) {
}
