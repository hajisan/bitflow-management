package com.example.estimationtool.repository.interfaces;

import com.example.estimationtool.model.timeEntry.TimeEntry;

import java.util.List;

public interface ITimeEntryRepository extends IRepository<TimeEntry, Integer> {
    TimeEntry create(TimeEntry timeEntry);

    List<TimeEntry> readAll();

    TimeEntry readById(Integer id);

    TimeEntry update(TimeEntry timeEntry);

    void deleteById(Integer id);
}
