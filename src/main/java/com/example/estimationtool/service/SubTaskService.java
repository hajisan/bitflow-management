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

    public SubTask readById(int id) {
        return iSubTaskRepository.readById(id);
    }

    //------------------------------------ Update() ------------------------------------

   public SubTask updateSubTask(SubTask subtask) {
        return iSubTaskRepository.update(subtask);
   }

    //------------------------------------ Delete() ------------------------------------

    public void deleteById(int id) {
        iSubTaskRepository.deleteById(id);
    }
}
