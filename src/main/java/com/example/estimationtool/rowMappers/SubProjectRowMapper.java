package com.example.estimationtool.rowMappers;

import com.example.estimationtool.enums.Status;
import com.example.estimationtool.subProject.SubProject;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SubProjectRowMapper implements RowMapper<SubProject> {
    @Override
    public SubProject mapRow(ResultSet rs, int rowNum) throws SQLException {
        SubProject subProject = new SubProject();
        subProject.setSubProjectId(rs.getInt("id"));
        subProject.setProjectId(rs.getInt("projectID"));
        subProject.setName(rs.getString("name"));
        subProject.setDescription(rs.getString("description"));
        // @TODO Constructoren tager en LocalDate og ikke en String!
        subProject.setDescription(rs.getString("deadline"));
        subProject.setEstimatedTime(rs.getInt("estimatedTime"));
        subProject.setTimeSpent(rs.getInt("timeSpent"));
        subProject.setStatus(Status.valueOf(rs.getString("status")));



        return null;
    }
}
