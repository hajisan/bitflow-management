package com.example.estimationtool.toolbox.rowMappers;

import com.example.estimationtool.enums.Status;
import com.example.estimationtool.model.Project;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ProjectRowMapper implements RowMapper<Project> {
    @Override
    public Project mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Project(
                rs.getInt("id"),
                rs.getInt("userId"),
                rs.getInt("estimatedTime"),
                rs.getInt("timeSpent"),
                rs.getString("name"),
                rs.getString("description"),
                LocalDate.parse(rs.getString("deadline")),
                Status.valueOf(rs.getString("status"))
        );
    }
}
