package com.example.estimationtool.toolbox.dto;

import com.example.estimationtool.model.SubProject;

import java.util.List;

public record UserWithSubProjectDTO(UserViewDTO user, List<SubProject> subProjectList) {
}
