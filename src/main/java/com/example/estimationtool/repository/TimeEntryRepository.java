package com.example.estimationtool.repository;

import com.example.estimationtool.model.timeEntry.TimeEntry;
import com.example.estimationtool.repository.interfaces.ITimeEntryRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
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

        String sql = """
                INSERT INTO timeentry (userID, date, hoursSpent)
                VALUES (?, ?, ?)
                """;


        KeyHolder keyHolder = new GeneratedKeyHolder();

        // Bruger PreparedStatement sammen med vores GeneratedKeyHolder til at kunne autogenerere et nyt id

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setInt(1, timeEntry.getUserId());
            ps.setDate(2, java.sql.Date.valueOf(timeEntry.getDate()));
            ps.setInt(3, timeEntry.getHoursSpent());
            return ps;

            }, keyHolder);

        int generatedId = keyHolder.getKey().intValue();
        timeEntry.setTimeId(generatedId);  // Sætter ID på timeEntry

        // Indsætter de eksisterende ID'er fra Task og SubTask
        jdbcTemplate.update("INSERT INTO timeentry_task (timeEntryID, taskID) VALUES (?, ?)",
                generatedId, timeEntry.getTaskId());

        jdbcTemplate.update("INSERT INTO timeentry_subtask (timeEntryID, subTaskID) VALUES (?, ?)",
                generatedId, timeEntry.getSubTaskId());

        return timeEntry;
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
