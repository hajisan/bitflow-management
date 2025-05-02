package com.example.estimationtool.dto;


import com.example.estimationtool.project.Project;
import com.example.estimationtool.subProject.SubProject;

import java.util.List;

public record ProjectWithSubProjectsDTO(Project project, List<SubProject> subProjectList) {
}
