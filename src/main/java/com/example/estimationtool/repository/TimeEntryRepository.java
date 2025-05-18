package com.example.estimationtool.repository;

import com.example.estimationtool.model.timeEntry.TimeEntry;
import com.example.estimationtool.repository.interfaces.ITimeEntryRepository;
import com.example.estimationtool.toolbox.rowMappers.TimeEntryRowMapper;
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
                INSERT INTO timeentry (userID, taskID, subtaskID, date, hoursSpent)
                VALUES (?, ?, ?, ?, ?)
                """;


        KeyHolder keyHolder = new GeneratedKeyHolder();

        // Bruger PreparedStatement sammen med vores GeneratedKeyHolder til at kunne autogenerere et nyt id

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setInt(1, timeEntry.getUserId());
            ps.setInt(2, timeEntry.getTaskId());
            ps.setInt(3, timeEntry.getSubTaskId());
            ps.setDate(4, java.sql.Date.valueOf(timeEntry.getDate()));
            ps.setInt(5, timeEntry.getHoursSpent());
            return ps;

            }, keyHolder);

        int generatedId = keyHolder.getKey().intValue();
        timeEntry.setTimeId(generatedId);  // Sætter ID på timeEntry

        return timeEntry;
    }

    //------------------------------------ Read() --------------------------------------

    @Override
    public List<TimeEntry> readAll() {

        // JOIN bruges til at hente data fra de øvrige tabeller: timeentry_subtask og timeentry_task

        String sql = """
                SELECT
                timeentry.id,
                timeentry.userID,
                timeentry.taskID,
                timeentry.subtaskID,
                timeentry.date,
                timeentry.hoursSpent
                FROM timeentry
                """; // Finder dét taskID/subtaskID, der hører til en timeEntry
        return jdbcTemplate.query(sql, new TimeEntryRowMapper());
    }

    @Override
    public TimeEntry readById(Integer id) {

        String sql = """
                SELECT
                timeentry.id,
                timeentry.userID,
                timeentry.taskID,
                timeentry.subtaskID,
                timeentry.date,
                timeentry.hoursSpent
                FROM timeentry
                WHERE timeentry.id = ?
                """; // Finder task/subtask for en timeEntry
        return jdbcTemplate.queryForObject(sql, new TimeEntryRowMapper(), id);
    }

    //------------------------------------ Update() ------------------------------------

    @Override
    public TimeEntry update(TimeEntry timeEntry) {

        String sql = """
                UPDATE timeentry
                SET userID = ?, taskID = ?, subtaskID = ?, date = ?, hoursSpent = ?
                WHERE id = ?
                """;

        jdbcTemplate.update(sql,
                timeEntry.getUserId(),
                timeEntry.getTaskId(),
                timeEntry.getSubTaskId(),
                java.sql.Date.valueOf(timeEntry.getDate()),
                timeEntry.getHoursSpent(),
                timeEntry.getTimeId()
        );

        return timeEntry;

    }

    //------------------------------------ Delete() ------------------------------------

    @Override
    public void deleteById(Integer id) {

        String sql = "DELETE FROM timeentry WHERE id = ?";

        jdbcTemplate.update(sql, id);

    }

    //-------------------------------- Til TimeSpent -----------------------------------
//
//    public int sumTimeSpentBySubTaskId(int subTaskId) {
//        String sql = """
//                SELECT COALESCE(SUM(timeSpent), 0)
//                FROM timeentry
//                WHERE
//                """;
//    }

    //---------------------------------- Til DTO'er ------------------------------------

    // --- Read() timeentries ud fra task-ID -----

    @Override
    public List<TimeEntry> readAllByTaskId(Integer taskId) {

        String sql = """
        SELECT
            timeentry.id,
            timeentry.userID,
            timeentry.taskID,
            timeentry.subtaskID,
            timeentry.date,
            timeentry.hoursSpent
        FROM timeentry
        WHERE timeentry.taskID = ?
        """;

        return jdbcTemplate.query(sql, new TimeEntryRowMapper(), taskId);
    }


    // --- Read() timeentries ud fra subtask-ID ---

    @Override
    public List<TimeEntry> readAllBySubTaskId(Integer subTaskId) {

        String sql = """
        SELECT
            timeentry.id,
            timeentry.userID,
            timeentry.taskID,
            timeentry.subtaskID,
            timeentry.date,
            timeentry.hoursSpent
        FROM timeentry
        WHERE timeentry.subTaskID = ?
        """;

        return jdbcTemplate.query(sql, new TimeEntryRowMapper(), subTaskId);
    }
}
