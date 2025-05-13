package com.example.estimationtool.repository.interfaces;
import com.example.estimationtool.model.SubTask;
import java.util.List;

public interface ISubTaskRepository extends IRepository<SubTask, Integer> {

    // CRUDS

    SubTask create(SubTask subTask);

    List<SubTask> readAll();

    SubTask readById(Integer subtaskId);

    SubTask update(SubTask subTask);

    void deleteById(Integer subTaskId);

    // CRUDS for DTO'er

    List<SubTask> readAllByUserId(Integer userId);

}


