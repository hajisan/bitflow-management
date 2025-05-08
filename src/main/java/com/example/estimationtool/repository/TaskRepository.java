package com.example.estimationtool.repository;

import com.example.estimationtool.repository.interfaces.ITaskRepository;
import com.example.estimationtool.model.Task;
import com.example.estimationtool.toolbox.rowMappers.TaskRowMapper;
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

        int generatedId = keyHolder.getKey().intValue();
        task.setTaskId(generatedId);  // Sæt det genererede ID i objektet

        return task;
    }

    //------------------------------------ Read() ------------------------------------
    @Override
    public List<Task> readAll() {

        String sql = "SELECT id, subProjectID, name, description, deadline, estimatedTime, status FROM task";

        return jdbcTemplate.query(sql, new TaskRowMapper());
    }

    @Override
    public Task readById(Integer id) {

        String sql = """
        SELECT
        id, subProjectID, name, description, deadline, estimatedTime, status
        FROM task
        WHERE id = ?
        """;
        return jdbcTemplate.queryForObject(sql, new TaskRowMapper(), id);
    }

    //------------------------------------ Update() ------------------------------------

    @Override
    public Task update(Task task) {

        String sql = """
        UPDATE task
        SET name = ?, description = ?, deadline = ?, estimatedTime = ?, status = ?
        WHERE id = ?
        """;

        jdbcTemplate.update( // Henter disse værdier, så de kan opdateres
                sql,
                task.getName(),
                task.getDescription(),
                task.getDeadline(),
                task.getEstimatedTime(),
                task.getStatus().name(), // Konverteres til String for at gemmes i databasen
                task.getTaskId()); // Parameter -> id til WHERE

        return task;
    }

    //------------------------------------ Delete() ------------------------------------

    @Override
    public void deleteById(Integer id) {

        String sql = "DELETE FROM task WHERE id = ?";
        jdbcTemplate.update(sql, id);

    }
}
