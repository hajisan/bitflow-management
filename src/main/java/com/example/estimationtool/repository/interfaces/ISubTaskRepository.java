package com.example.estimationtool.repository.interfaces;
import com.example.estimationtool.model.SubTask;
import java.util.List;

public interface ISubTaskRepository extends IRepository<SubTask, Integer> {

    // CRUDS

    SubTask create(SubTask subTask);

    List<SubTask> readAll();

    SubTask readById(Integer id);

    SubTask update(SubTask subTask);

    void deleteById(Integer id);

    // CRUDS for DTO'er

}


