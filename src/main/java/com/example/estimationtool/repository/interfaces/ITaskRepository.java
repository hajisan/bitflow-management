package com.example.estimationtool.repository.interfaces;

import com.example.estimationtool.model.Task;
import com.example.estimationtool.model.User;

import java.util.List;

public interface ITaskRepository extends IRepository<Task, Integer> {

    // CRUDS

    Task create(Task task);

    List<Task> readAll();

    Task readById(Integer taskId);

    Task update(Task task);

    void deleteById(Integer taskId);

    // CRUDS for DTO'er

    List<Task> readAllByUserId(Integer userId);

    List<Task> readAllBySubProjectId(Integer subProjectId);

    // Kobler User til Task i mellemtabellen
    void assignUserToTask(Integer userId, Integer taskId);

}
