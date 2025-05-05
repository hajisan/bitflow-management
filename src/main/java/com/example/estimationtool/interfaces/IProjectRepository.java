package com.example.estimationtool.interfaces;

import com.example.estimationtool.project.Project;
import com.example.estimationtool.project.ProjectRepository;

import java.util.List;

public interface IProjectRepository extends IRepository<Project, Integer> {

    Project create(Project project);

    List<Project> readAll();

    Project readById(int id);

    Project update(Project t);

    void deleteById(int id);
}
