package com.example.estimationtool.toolbox.rowMappers;

import com.example.estimationtool.model.enums.Status;
import com.example.estimationtool.model.Task;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class TaskRowMapper implements RowMapper<Task> {
    @Override
    public Task mapRow(ResultSet rs, int rowNum) throws SQLException {

        java.sql.Date sqlDate = rs.getDate("deadline");
        LocalDate deadline = null; // Sætter deadline til null

        if (sqlDate != null) {
            deadline = sqlDate.toLocalDate(); // Hvis deadline ikke er null, konverter til LocalDate
        }
        return new Task(
                rs.getInt("subProjectID"),
                rs.getInt("id"),
                rs.getInt("estimatedTime"),
                rs.getInt("timeSpent"),
                rs.getString("name"),
                rs.getString("description"),
                deadline,
                Status.valueOf(rs.getString("status"))
        );
    }
}
