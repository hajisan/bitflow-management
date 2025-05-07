package com.example.estimationtool.repository;

import com.example.estimationtool.model.SubTask;
import com.example.estimationtool.repository.interfaces.ISubTaskRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SubTaskRepository implements ISubTaskRepository {

    private final JdbcTemplate jdbcTemplate;

    public SubTaskRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public SubTask create(SubTask SubTask) {
        return null;
    }

    @Override
    public List<SubTask> readAll() {
        return List.of();
    }

    @Override
    public SubTask readById(Integer id) {
        return null;
    }

    @Override
    public SubTask update(SubTask subTask) {
        return null;
    }

    @Override
    public void deleteById(Integer id) {

    }
}
