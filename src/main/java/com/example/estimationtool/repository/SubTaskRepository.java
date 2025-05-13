package com.example.estimationtool.repository;

import com.example.estimationtool.model.SubTask;
import com.example.estimationtool.repository.interfaces.ISubTaskRepository;
import com.example.estimationtool.toolbox.rowMappers.SubTaskRowMapper;
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

    //------------------------------------ Create() ------------------------------------

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

        int generatedId = keyHolder.getKey().intValue();
        subTask.setTaskId(generatedId);  // Sæt det genererede ID i objektet

        return subTask;
    }

    //------------------------------------ Read() --------------------------------------


    @Override
    public List<SubTask> readAll() {

        String sql = """
                SELECT id, taskID, estimatedTime, name, description, deadline, status
                FROM subtask
                """;

        return jdbcTemplate.query(sql, new SubTaskRowMapper());
    }

    @Override
    public SubTask readById(Integer id) {

        String sql = """
                SELECT
                id, taskID, estimatedTime, name, description, deadline, status
                FROM subtask
                WHERE id = ?
                """;
        return jdbcTemplate.queryForObject(sql, new SubTaskRowMapper(), id);
    }

    //------------------------------------ Update() ------------------------------------


    @Override
    public SubTask update(SubTask subTask) {

        String sql = """
                UPDATE subtask
                SET estimatedTime = ?, name = ?, description = ?, deadline = ?, status = ?
                WHERE id = ?
                """;

        jdbcTemplate.update( // Henter disse værdier, så de kan opdateres
             sql,
             subTask.getEstimatedTime(),
             subTask.getName(),
             subTask.getDescription(),
             subTask.getDeadline(),
             subTask.getStatus().name(), // Konverteres til String for at gemmes i databasen
             subTask.getSubTaskId()); // Parameter -> id til WHERE

        return subTask;
    }

    //------------------------------------ Delete() ------------------------------------


    @Override
    public void deleteById(Integer id) {

        String sql = "DELETE FROM subtask WHERE id = ?";
        jdbcTemplate.update(sql, id);

    }

    //---------------------------------- Til DTO'er ------------------------------------


    // --- Read() subtasks ud fra bruger-ID ---
    @Override
    public List<SubTask> readAllByUserId(Integer userId) {

        // Bruger JOIN til at hente alle subtasks for ét brugerID

        String sql = """
                SELECT
                    subtask.id,
                    subtask.taskID,
                    subtask.estimatedTime,
                    subtask.name,
                    subtask.description,
                    subtask.deadline,
                    subtask.status
                FROM subtask
                JOIN user_subtask ON subtask.id = user_subtask.subTaskID
                WHERE user_subtask.userID = ?
                """;
        return jdbcTemplate.query(sql, new SubTaskRowMapper(), userId);
    }

    //---------------------------------- Assign User --------------------------------



    // ----------------- SubTask tildeles en bruger efter oprettelse ----------------

    @Override
    public void assignUserToSubTask(Integer userId, Integer subTaskId) {

        String sql = "INSERT INTO user_subtask (userID, subTaskID) VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, subTaskId);

    }


}
