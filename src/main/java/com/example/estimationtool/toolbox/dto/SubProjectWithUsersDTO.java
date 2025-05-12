package com.example.estimationtool.toolbox.dto;

import com.example.estimationtool.model.SubProject;

import java.util.List;

public record SubProjectWithUsersDTO(SubProject subProject, List<UserViewDTO> userViewDTOList) {
}
