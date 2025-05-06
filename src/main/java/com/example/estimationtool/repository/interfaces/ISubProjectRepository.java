package com.example.estimationtool.repository.interfaces;

import com.example.estimationtool.model.SubProject;

import java.util.List;

public interface ISubProjectRepository extends IRepository<SubProject, Integer>{

    SubProject create(SubProject subProject);

    List<SubProject> readAll();

    SubProject readById(Integer id);

    SubProject update(SubProject subProject);

    void deleteById(Integer id);
}