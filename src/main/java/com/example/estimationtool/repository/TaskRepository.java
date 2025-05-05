package com.example.estimationtool.repository;

import com.example.estimationtool.interfaces.ITaskRepository;
import com.example.estimationtool.model.Task;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class TaskRepository implements ITaskRepository {

    private final JdbcTemplate jdbcTemplate;

    public TaskRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //------------------------------------ Create() ------------------------------------

    @Override
    public Task create(Task task) {

        String sql = """
        INSERT INTO task (subProjectID, name, description, deadline, estimatedTime, status) VALUES (?, ?, ?, ?, ?, ?)
        """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        // Bruger PreparedStatement sammen med vores GeneratedKeyHolder til at kunne autogenerere et nyt id
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setInt(1, task.getSubProjectId());
            ps.setString(2, task.getName());
            ps.setString(3, task.getDescription());
            ps.setDate(4, java.sql.Date.valueOf(task.getDeadline())); // Konverterer LocalDate til java.sql.Date
            ps.setInt(5, task.getEstimatedTime());
            ps.setString(6, task.getStatus().name());
            return ps;
        }, keyHolder);

        return task;
    }

    //------------------------------------ Read() ------------------------------------
    @Override
    public List<Task> readAll() {
        return List.of();
    }

    @Override
    public Task readById(int id) {
        return null;
    }

    //------------------------------------ Update() ------------------------------------

    @Override
    public Task update(Task task) {
        return null;
    }

    //------------------------------------ Delete() ------------------------------------

    @Override
    public void deleteById(int id) {

    }
}
