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
                rs.getInt("id"),
                rs.getInt("subProjectID"),
                rs.getInt("estimatedTime"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("deadline").toLocalDate(),
                Status.valueOf(rs.getString("status"))
        );
    }
}
