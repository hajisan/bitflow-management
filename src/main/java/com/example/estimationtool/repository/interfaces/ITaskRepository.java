package com.example.estimationtool.interfaces;

import com.example.estimationtool.model.Task;
import com.example.estimationtool.model.User;

import java.util.List;

public interface ITaskRepository extends IRepository<Task, Integer> {

    Task create(Task task);

    List<Task> readAll();

    Task readById(int id);

    Task update(Task task);

    void deleteById(int id);

}
