package com.example.estimationtool.toolbox.rowMappers;

import com.example.estimationtool.model.timeEntry.TimeEntry;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TimeEntryRowMapper implements RowMapper<TimeEntry> {
    @Override
    public TimeEntry mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new TimeEntry(
                rs.getInt("id"),
                rs.getInt("userID"),
                rs.getInt("taskID"),
                rs.getInt("subTaskID"),
                rs.getInt("hoursSpent"),
                rs.getDate("date").toLocalDate()

        );
    }
}
