package com.example.estimationtool.service;


import com.example.estimationtool.repository.interfaces.ITaskRepository;
import com.example.estimationtool.model.Task;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final ITaskRepository iTaskRepository;

    public TaskService(ITaskRepository iTaskRepository) {
        this.iTaskRepository = iTaskRepository;
    }

    //------------------------------------ Create() ------------------------------------

    public Task createTask(Task task) {
        return iTaskRepository.create(task);

    }

    //------------------------------------ Read() --------------------------------------

    public List<Task> readAll() {
        return iTaskRepository.readAll();
    }

    public Task readById(int id) {
        return iTaskRepository.readById(id);
    }

    //------------------------------------ Update() ------------------------------------

    public Task updateTask(Task task) {
        return iTaskRepository.update(task);
    }
    //------------------------------------ Delete() ------------------------------------

    public void deleteById(int id ) {
        iTaskRepository.deleteById(id);
    }

    //------------------------------------ DTO'er -------------------------------------

    // --- Henter tasks ud fra brugerID ---

    public List<Task> readAllTasksByUserId(int userId) {

        return iTaskRepository.readAllTasksByUserId(userId);
    }

}
