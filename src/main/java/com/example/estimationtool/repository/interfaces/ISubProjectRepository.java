package com.example.estimationtool.repository.interfaces;

import com.example.estimationtool.model.SubProject;
import com.example.estimationtool.toolbox.dto.ProjectWithSubProjectsDTO;

import java.util.List;

public interface ISubProjectRepository extends IRepository<SubProject, Integer>{

    SubProject create(SubProject subProject);

    List<SubProject> readAll();

    SubProject readById(Integer id);

    SubProject update(SubProject subProject);

    void deleteById(Integer id);

    // Ikke fra IRepository
    ProjectWithSubProjectsDTO readAllFromProjectId(int projectId);
}