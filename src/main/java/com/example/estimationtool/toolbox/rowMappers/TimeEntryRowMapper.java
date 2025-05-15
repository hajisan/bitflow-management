package com.example.estimationtool.toolbox.rowMappers;

import com.example.estimationtool.model.timeEntry.TimeEntry;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

// Opretter TimeEntry-instans for hver kolonne i de tre tabeller

public class TimeEntryRowMapper implements RowMapper<TimeEntry> {
    @Override
    public TimeEntry mapRow(ResultSet rs, int rowNum) throws SQLException {
        java.sql.Date sqlDate = rs.getDate("date");
        LocalDate date = null; // Sætter date til null

        if (sqlDate != null) {
            date = sqlDate.toLocalDate(); // Hvis date ikke er null, konverter til LocalDate
        }
        return new TimeEntry(
                rs.getInt("id"),
                rs.getInt("userID"),
                rs.getInt("taskID"),
                rs.getInt("subTaskID"),
                rs.getInt("hoursSpent"),
                date

        );
    }
}
