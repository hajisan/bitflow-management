package com.example.estimationtool.repository.interfaces;

import com.example.estimationtool.model.Project;

import java.util.List;

public interface IProjectRepository extends IRepository<Project, Integer> {

    // CRUDS

    Project create(Project project);

    List<Project> readAll();

    Project readById(Integer id);

    Project update(Project project);

    void deleteById(Integer id);

    // CRUDS for DTO'er

    List<Project> readAllByUserId(Integer userId);

    void assignUserToProject(Integer userId, Integer projectId);
}
