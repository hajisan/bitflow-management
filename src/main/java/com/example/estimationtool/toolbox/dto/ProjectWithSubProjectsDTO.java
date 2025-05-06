package com.example.estimationtool.toolbox.dto;


import com.example.estimationtool.model.Project;
import com.example.estimationtool.model.SubProject;

import java.util.List;

public record ProjectWithSubProjectsDTO(Project project, List<SubProject> subProjectList) {
}
