package com.example.estimationtool.toolbox.dto;

import com.example.estimationtool.model.Project;

import java.util.List;

public record ProjectWithUsersDTO(Project project, List<UserViewDTO> userViewDTOList) {
}
