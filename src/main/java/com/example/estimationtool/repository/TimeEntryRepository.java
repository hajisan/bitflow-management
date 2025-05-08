package com.example.estimationtool.repository;

import com.example.estimationtool.model.timeEntry.TimeEntry;
import com.example.estimationtool.repository.interfaces.ITimeEntryRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TimeEntryRepository implements ITimeEntryRepository {

    private final JdbcTemplate jdbcTemplate;

    public TimeEntryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    //------------------------------------ Create() ------------------------------------

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        return null;
    }

    //------------------------------------ Read() --------------------------------------

    @Override
    public List<TimeEntry> readAll() {
        return List.of();
    }

    @Override
    public TimeEntry readById(Integer id) {
        return null;
    }

    //------------------------------------ Update() ------------------------------------

    @Override
    public TimeEntry update(TimeEntry timeEntry) {
        return null;
    }

    //------------------------------------ Delete() ------------------------------------

    @Override
    public void deleteById(Integer id) {

    }


}
