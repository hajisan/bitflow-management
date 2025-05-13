//package com.example.estimationtool.toolbox.rowMappers;
//
//import com.example.estimationtool.model.enums.Status;
//import com.example.estimationtool.model.SubProject;
//import com.example.estimationtool.model.timeEntry.DateHandler;
//import org.springframework.jdbc.core.RowMapper;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//public class SubProjectRowMapper implements RowMapper<SubProject> {
//    @Override
//    public SubProject mapRow(ResultSet rs, int rowNum) throws SQLException {
//        SubProject subProject = new SubProject();
//        subProject.setSubProjectId(rs.getInt("id"));
//        subProject.setProjectId(rs.getInt("projectID"));
//        subProject.setName(rs.getString("name"));
//        subProject.setDescription(rs.getString("description"));
//        subProject.setDeadline(rs.getDate("deadline").toLocalDate());
//        subProject.setEstimatedTime(rs.getInt("estimatedTime"));
//        subProject.setTimeSpent(rs.getInt("timeSpent"));
//        subProject.setStatus(Status.valueOf(rs.getString("status")));
//
//        return subProject;
//    }
//}
