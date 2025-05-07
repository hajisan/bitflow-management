package com.example.estimationtool.service;


import com.example.estimationtool.model.SubTask;
import com.example.estimationtool.repository.interfaces.ISubTaskRepository;
import org.springframework.stereotype.Service;

@Service
public class SubTaskService {

    private final ISubTaskRepository iSubTaskRepository;

    public SubTaskService(ISubTaskRepository iSubTaskRepository) {
        this.iSubTaskRepository = iSubTaskRepository;
    }

    //------------------------------------ Create() ------------------------------------

    public SubTask createSubTask(SubTask subTask) {
        return iSubTaskRepository.create(subTask);
    }

    //------------------------------------ Read() --------------------------------------

    //------------------------------------ Update() ------------------------------------

    //------------------------------------ Delete() ------------------------------------

}
