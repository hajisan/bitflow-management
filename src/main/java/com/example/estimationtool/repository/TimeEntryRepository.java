package com.example.estimationtool.repository;

import com.example.estimationtool.model.timeEntry.TimeEntry;
import com.example.estimationtool.repository.interfaces.ITimeEntryRepository;
import com.example.estimationtool.toolbox.rowMappers.TimeEntryRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Time;
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

        // JOIN bruges til at hente data fra de øvrige tabeller: timeentry_subtask og timeentry_task

        String sql = """
                SELECT
                timeentry.id,
                timeentry.userID,
                timeentry.date,
                timeentry.hoursSpent,
                timeentry_task.taskID,
                timeentry_subtask.subTaskID
                FROM timeentry
                JOIN timeentry_task ON timeentry.id = timeentry_task.timeEntryID
                JOIN timeentry_subtask ON timeentry.id = timeentry_subtask.timeEntryID
                """; // Finder dét taskID/subtaskID, der hører til en timeentry
        return jdbcTemplate.query(sql, new TimeEntryRowMapper());
    }

    @Override
    public TimeEntry readById(Integer id) {

        String sql = """
                SELECT
                timeentry.id,
                timeentry.userID,
                timeentry.date,
                timeentry.hoursSpent,
                timeentry_task.taskID,
                timeentry_subtask.subTaskID
                FROM timeentry
                JOIN timeentry_task ON timeentry.id = timeentry_task.timeEntryID
                JOIN timeentry_subtask ON timeentry.id = timeentry_subtask.timeEntryID
                WHERE timeentry.id = ?
                """; // Finder task/subtask for en timeEntry
        return jdbcTemplate.queryForObject(sql, new TimeEntryRowMapper(), id);
    }

    //------------------------------------ Update() ------------------------------------

    @Override
    public TimeEntry update(TimeEntry timeEntry) {

        String sql = """
                UPDATE timeentry
                SET userID = ?, date = ?, hoursSpent = ?
                WHERE id = ?
                """;

        jdbcTemplate.update(sql,
                timeEntry.getUserId(),
                java.sql.Date.valueOf(timeEntry.getDate()),
                timeEntry.getHoursSpent(),
                timeEntry.getTimeId()
        );

        // Opdaterer timeEntry i tabellen: timeentry_task
        String sqlTask = """
                UPDATE timeentry_task
                SET taskID = ?
                WHERE timeEntryID = ?
                """;


        jdbcTemplate.update(sqlTask,
                timeEntry.getTaskId(),
                timeEntry.getTimeId()
        );

        // Opdaterer timeEntry i tabellen: timeentry_subtask
        String sqlSubTask = """
                UPDATE timeentry_subtask
                SET subTaskID = ?
                WHERE timeEntryID = ?
                """;

        jdbcTemplate.update(sqlSubTask,
                timeEntry.getSubTaskId(),
                timeEntry.getTimeId()
        );

        return timeEntry;

    }

    //------------------------------------ Delete() ------------------------------------

    @Override
    public void deleteById(Integer id) {

        String sqlSubTask = "DELETE FROM timeentry_subtask WHERE timeEntryID = ?";

        String sqlTask = "DELETE FROM timeentry_task WHERE timeEntryID = ?";



        String sqlTimeEntry = "DELETE FROM timeentry WHERE id = ?";

        //Relationstabeller slettes FØR timeEntry (ellers = foreign key violation)
        jdbcTemplate.update(sqlSubTask, id);
        jdbcTemplate.update(sqlTask, id);
        jdbcTemplate.update(sqlTimeEntry, id);

    }


}
