package com.example.estimationtool.repository.interfaces;

import com.example.estimationtool.model.TimeEntry;

import java.util.List;

public interface ITimeEntryRepository extends IRepository<TimeEntry, Integer> {

    // CRUDS

    TimeEntry create(TimeEntry timeEntry);

    List<TimeEntry> readAll();

    TimeEntry readById(Integer id);

    TimeEntry update(TimeEntry timeEntry);

    void deleteById(Integer id);

    // CRUDS for DTO'er

    List<TimeEntry> readAllByTaskId(Integer taskId);

    List<TimeEntry> readAllBySubTaskId(Integer subTaskId);



}
