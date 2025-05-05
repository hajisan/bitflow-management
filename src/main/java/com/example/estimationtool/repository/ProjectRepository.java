package com.example.estimationtool.project;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;

@Repository
public class ProjectRepository {

    private final JdbcTemplate jdbcTemplate;

    // Injection af JdbcTemplate til databasekald
    public ProjectRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //------------------------------------ Create() ------------------------------------

    public Project create(Project project) {

        String sql = "INSERT INTO project (name, description, deadline, estimatedTime, timeSpent, status) VALUES (?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, project.getName());
            ps.setString(2, project.getDescription());
            ps.setDate(3, java.sql.Date.valueOf(project.getDeadLine())); // Sætter deadline som SQL-dato
            ps.setInt(4, project.getEstimatedTime());
            ps.setInt(5, project.getTimeSpent());
            ps.setString(6, project.getStatus().name());
            return ps;
        }, keyHolder);

        // Tjekker først om id'et er null eller ej. Hvis det er, så sætter vi id-variable til -1
        int projectId = keyHolder.getKey() != null ? keyHolder.getKey().intValue() : -1;
        // Sætter projektets id til KeyHolderens værdi, hvis den ikke var null
        if (projectId != -1) project.setProjectId(projectId);

        return project;
    }
}
