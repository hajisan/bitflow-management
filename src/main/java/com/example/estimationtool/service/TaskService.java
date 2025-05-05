package com.example.estimationtool.service;


import com.example.estimationtool.interfaces.ITaskRepository;
import com.example.estimationtool.model.Task;
import com.example.estimationtool.repository.TaskRepository;
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


        //------------------------------------ Update() ------------------------------------

        //------------------------------------ Delete() ------------------------------------


        //------------------------------------ Create() ------------------------------------


        //------------------------------------ Read() --------------------------------------

        //------------------------------------ Update() ------------------------------------

        //------------------------------------ Delete() ------------------------------------


}
