package com.example.estimationtool.repository;

import com.example.estimationtool.toolbox.rowMappers.ProjectRowMapper;
import com.example.estimationtool.repository.interfaces.IProjectRepository;
import com.example.estimationtool.model.Project;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class ProjectRepository implements IProjectRepository {

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

    //------------------------------------ Read() ------------------------------------

    @Override
    public List<Project> readAll() {

        /*
        TODO
        SELECT p.userId gør at Admin henter alle projekter altså, hvilket resultere i at Admin ser samme projekt flere gange
        da et projekt kan have flere brugere tilknyttet i user_project tabellen.
        Formål er dog at Admin kun skal kunne se listen over alle projekter, uden afhængigheden af user_project tabellen.
        Så Admin kun se en liste af Projekter uden duplikater. Se seneste chat for forslag til at løse det.
         */

        String sql = """
                SELECT DISTINCT p.id, p.name, p.description, p.deadline, p.estimatedTime, p.timeSpent, p.status, up.userID as userId
                FROM project p
                LEFT JOIN user_project up ON p.id = up.projectID
                """;

        return jdbcTemplate.query(sql, new ProjectRowMapper());
    }

    @Override
    public Project readById(Integer id) {

        String sql = """
                SELECT p.id, p.name, p.description, p.deadline, p.estimatedTime, p.timeSpent, p.status, up.userID as userId
                FROM project p
                LEFT JOIN user_project up ON p.id = up.projectID
                WHERE p.id = ?
                """;

        try {
            return jdbcTemplate.queryForObject(sql, new ProjectRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Project> readByUserId(Integer userId) {

        String sql = """
                SELECT p.id, p.name, p.description, p.deadline, p.estimatedTime, p.timeSpent, p.status, up.userID as userId
                FROM project p
                LEFT JOIN user_project up ON p.id = up.projectID
                WHERE up.userID = ?
                """;

        return jdbcTemplate.query(sql, new ProjectRowMapper(), userId);
    }

    //------------------------------------ Update() ------------------------------------

    @Override
    public Project update(Project project) {
        return null;
    }

    //------------------------------------ Delete() ------------------------------------

    @Override
    public void deleteById(Integer id) {

    }
}

