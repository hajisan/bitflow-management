package com.example.estimationtool.dto;

import com.example.estimationtool.subProject.SubProject;

import java.util.List;

public record UserWithSubProjectDTO(UserViewDTO userViewDTO, List<SubProject> subProjectList) {
}
