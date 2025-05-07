
package com.example.estimationtool.repository;

import com.example.estimationtool.model.SubTask;
import com.example.estimationtool.repository.interfaces.ISubTaskRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class SubTaskRepository implements ISubTaskRepository {

    private final JdbcTemplate jdbcTemplate;

    public SubTaskRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public SubTask create(SubTask subTask) {

        String sql = """
                INSERT INTO
                subtask (taskID, name, description, deadline, estimatedTime, status)
                VALUES (?, ?, ?, ?, ?, ?)
                """;


        KeyHolder keyHolder = new GeneratedKeyHolder();

        // Bruger PreparedStatement sammen med vores GeneratedKeyHolder til at kunne autogenerere et nyt id
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setInt(1, subTask.getTaskId());
            ps.setString(2, subTask.getName());
            ps.setString(3, subTask.getDescription());
            ps.setDate(4, java.sql.Date.valueOf(subTask.getDeadline()));// Konverterer LocalDate til java.sql.Date
            ps.setInt(5, subTask.getEstimatedTime());
            ps.setString(6, subTask.getStatus().name());
            return ps;
            }, keyHolder);

        return subTask;
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
