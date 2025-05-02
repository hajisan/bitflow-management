package com.example.estimationtool.dto;

import com.example.estimationtool.subProject.SubProject;

import java.util.List;

public record UserWithSubProjectDTO(UserViewDTO user, List<SubProject> subProjectList) {
}
