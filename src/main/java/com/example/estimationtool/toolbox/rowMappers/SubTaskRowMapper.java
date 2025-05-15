package com.example.estimationtool.toolbox.rowMappers;

import com.example.estimationtool.model.SubTask;
import com.example.estimationtool.model.enums.Status;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SubTaskRowMapper implements RowMapper<SubTask> {
    @Override
    public SubTask mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new SubTask(
                rs.getInt("id"),
                rs.getInt("taskID"),
                rs.getInt("estimatedTime"),
                rs.getInt("timeSpent"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("deadline").toLocalDate(),
                Status.valueOf(rs.getString("status"))
        );

    }
}
