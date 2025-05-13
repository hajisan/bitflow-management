package com.example.estimationtool.repository.interfaces;

import com.example.estimationtool.model.SubProject;
import com.example.estimationtool.toolbox.dto.ProjectWithSubProjectsDTO;

import java.util.List;

public interface ISubProjectRepository extends IRepository<SubProject, Integer>{

    // CRUDS
    SubProject create(SubProject subProject);

    List<SubProject> readAll();

    SubProject readById(Integer subProjectId);

    SubProject update(SubProject subProject);

    void deleteById(Integer subProjectId);

    // CRUDS for DTO'er

    List<SubProject> readAllFromProjectId(Integer projectId);

    List<SubProject> readAllByUserId(Integer userId);
}