package com.example.estimationtool.repository;

import com.example.estimationtool.model.Project;
import com.example.estimationtool.repository.interfaces.ISubProjectRepository;
import com.example.estimationtool.model.SubProject;
import com.example.estimationtool.toolbox.dto.ProjectWithSubProjectsDTO;
import com.example.estimationtool.toolbox.rowMappers.ProjectRowMapper;
//import com.example.estimationtool.toolbox.rowMappers.SubProjectRowMapper;
import com.example.estimationtool.toolbox.rowMappers.SubProjectRowMapperS;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
public class SubProjectRepository implements ISubProjectRepository {

    private final JdbcTemplate jdbcTemplate;

    public SubProjectRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //------------------------------------ Create() ------------------------------------

    @Override
    public SubProject create(SubProject subProject) {
        // String med SQL statement til JdbcTemplate
        String sql = "INSERT INTO subproject (projectID, name, description, deadline, estimatedTime, timeSpent, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

        // KeyHolder og PreparedStatement til at autogenerere ID'er ved indsættelse i databasen
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"}); // Giver ikke ID'et en værdi før til sidst
            ps.setString(1, Integer.toString(subProject.getProjectId()));
            ps.setString(2, subProject.getName());
            ps.setString(3, subProject.getDescription());
            ps.setString(4, subProject.getDeadline().format(DateTimeFormatter.ISO_LOCAL_DATE));
            ps.setString(5, Integer.toString(subProject.getEstimatedTime()));
            ps.setString(6, Integer.toString(0));
            ps.setString(7, subProject.getStatus().name());

            return ps;
        }, keyHolder);

        int id = keyHolder.getKey().intValue();
        subProject.setSubProjectId(id);

        return subProject;
    }


    //------------------------------------ Read() ------------------------------------

    @Override
    public List<SubProject> readAll() {
        String sql = """
                SELECT id, projectID, name, description, deadline, estimatedTime, timeSpent, status
                FROM subproject
                """;

        return jdbcTemplate.query(sql, new SubProjectRowMapperS());
    }


    @Override
    public SubProject readById(Integer subProjectId) {
        String sql = """
                SELECT id, projectID, name, description, deadline, estimatedTime, timeSpent, status
                FROM subproject
                WHERE id = ?
                """;
        return jdbcTemplate.query(sql, new SubProjectRowMapperS(), subProjectId).getFirst(); // Får en List<SubProject>, så skal kalde List.getFirst()
    }

    //------------------------------------ Update() ------------------------------------

    @Override
    public SubProject update(SubProject subProject) {
        String sql = """
                    UPDATE subproject
                    SET projectID = ?, name = ?, description = ?, deadline = ?, estimatedTime = ?, timeSpent = ?, status = ?
                    WHERE id = ?
                    """;

        jdbcTemplate.update( // Henter disse værdier, så de kan opdateres
                sql,
                subProject.getProjectId(),
                subProject.getName(),
                subProject.getDescription(),
                subProject.getDeadline(),
                subProject.getEstimatedTime(),
                subProject.getTimeSpent(),
                subProject.getStatus().name(),
                subProject.getSubProjectId());

        return subProject;
    }

    //------------------------------------ Delete() ------------------------------------

    @Override
    public void deleteById(Integer subProjectId) {
        String sql = "DELETE FROM subproject WHERE id = ?";
        jdbcTemplate.update(sql, subProjectId);
    }

    //---------------------------------- Til DTO'er ------------------------------------

    @Override
    public List<SubProject> readAllFromProjectId(Integer projectId) {
        String sql = """
                SELECT id, projectID, name, description, deadline, estimatedTime, timeSpent, status
                FROM subproject
                WHERE projectID = ?
                """;

        return jdbcTemplate.query(sql, new SubProjectRowMapperS(), projectId);
    }


    // --- Read() subprojekter ud fra bruger-ID ---

    @Override
    public List<SubProject> readAllByUserId(Integer userId) {

        // Bruger JOIN til at joine subprojekt-ID'et fra mellemtabellen til subprojekt-ID'et fra
        // subprojekt-tabellen, hvor brugerID matcher

        String sql = """
                SELECT
                    subproject.id,
                    subproject.projectID,
                    subproject.estimatedTime,
                    subproject.timeSpent,
                    subproject.name,
                    subproject.description,
                    subproject.deadline,
                    subproject.status
                FROM subproject
                JOIN user_subproject ON subproject.id = user_subproject.subProjectID
                WHERE user_subproject.userID = ?
                """;
        return jdbcTemplate.query(sql, new SubProjectRowMapperS(), userId);
    }

    //---------------------------------- Assign User ---------------------------------

    // ----------------- Subprojekt tildeles en bruger efter oprettelse --------------

    @Override
    public void assignUserToSubProject(Integer userId, Integer subProjectId) {
        String sql = "INSERT INTO user_subproject (userID, subProjectID) VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, subProjectId);
    }



}
