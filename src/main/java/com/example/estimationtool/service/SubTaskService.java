package com.example.estimationtool.service;


import com.example.estimationtool.model.SubTask;
import com.example.estimationtool.repository.interfaces.ISubTaskRepository;
import org.springframework.stereotype.Service;
import java.util.List;

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

    public List<SubTask> readAll() {
        return iSubTaskRepository.readAll();
    }

    //------------------------------------ Update() ------------------------------------

    public SubTask readById(int id) {
        return iSubTaskRepository.readById(id);
    }

    //------------------------------------ Delete() ------------------------------------

}
