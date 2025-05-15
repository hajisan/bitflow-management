package com.example.estimationtool.toolbox.rowMappers;

import com.example.estimationtool.model.enums.Status;
import com.example.estimationtool.model.Task;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TaskRowMapper implements RowMapper<Task> {
    @Override
    public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Task(
                rs.getInt("subProjectID"),
                rs.getInt("id"),
                rs.getInt("estimatedTime"),
                rs.getInt("timeSpent"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("deadline").toLocalDate(),
                Status.valueOf(rs.getString("status"))
        );
    }
}
