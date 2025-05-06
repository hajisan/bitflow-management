package com.example.estimationtool.repository.interfaces;

import com.example.estimationtool.model.Project;

import java.util.List;

public interface IProjectRepository extends IRepository<Project, Integer> {

    Project create(Project project);

    List<Project> readAll();

    Project readById(Integer id);

    Project update(Project project);

    void deleteById(Integer id);
}
