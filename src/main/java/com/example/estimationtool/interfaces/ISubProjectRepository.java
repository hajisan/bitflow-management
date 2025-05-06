package com.example.estimationtool.interfaces;

import com.example.estimationtool.model.Project;
import com.example.estimationtool.subProject.SubProject;

import java.util.List;

public interface ISubProjectRepository extends IRepository<SubProject, Integer>{

    SubProject create(SubProject subProject);

    List<SubProject> readAll();

    SubProject readById(int id);

    SubProject update(SubProject subProject);

    void deleteById(int id);
}
