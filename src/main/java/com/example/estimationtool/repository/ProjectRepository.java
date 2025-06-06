    package com.example.estimationtool.repository;

    import com.example.estimationtool.toolbox.rowMappers.ProjectRowMapper;
    import com.example.estimationtool.repository.interfaces.IProjectRepository;
    import com.example.estimationtool.model.Project;
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

        @Override
        public Project create(Project project) {

            String sql = "INSERT INTO project (name, description, deadline, estimatedTime, timeSpent, status) VALUES (?, ?, ?, ?, ?, ?)";

            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                ps.setString(1, project.getName());
                ps.setString(2, project.getDescription());
                ps.setDate(3, java.sql.Date.valueOf(project.getDeadline())); // Sætter deadline som SQL-dato
                ps.setInt(4, project.getEstimatedTime());
                ps.setInt(5, project.getTimeSpent());
                ps.setString(6, project.getStatus().name());
                return ps;
            }, keyHolder);

            int generatedId = keyHolder.getKey().intValue();
            project.setProjectId(generatedId);  // Sætter ID på Project

            return project;
        }

        //------------------------------------ Read() ------------------------------------

        @Override
        public List<Project> readAll() {

            String sql = """
                    SELECT id, estimatedTime, timeSpent, name, description, deadline, status
                    FROM project
                    """;

            return jdbcTemplate.query(sql, new ProjectRowMapper());
        }


        @Override
        public Project readById(Integer projectId) {

            String sql = """
                    SELECT id, estimatedTime, timeSpent, name, description, deadline, status
                    FROM project
                    WHERE id = ?
                    """;

            return jdbcTemplate.queryForObject(sql, new ProjectRowMapper(), projectId);

        }


        //------------------------------------ Update() ------------------------------------

        @Override
        public Project update(Project project) {
            String sql = """
                    UPDATE project
                    SET name = ?, description = ?, deadline = ?, estimatedTime = ?, timeSpent = ?, status = ?
                    WHERE id = ?
                    """;

            jdbcTemplate.update( // Henter disse værdier, så de kan opdateres
                    sql,
                    project.getName(),
                    project.getDescription(),
                    project.getDeadline(),
                    project.getEstimatedTime(),
                    project.getTimeSpent(),
                    project.getStatus().name(), // Konverteres til String for at gemme i databasen
                    project.getProjectId()); // Parameter -> id til WHERE

            return project;
        }

        //------------------------------------ Delete() ------------------------------------

        @Override
        public void deleteById(Integer id) {

            String sql = "DELETE FROM project WHERE id = ?";
            jdbcTemplate.update(sql, id);

        }


        //--------------------------------- Til DTO'er ------------------------------------

        // --- Read() projekter ud fra bruger-ID ---

        @Override
        public List<Project> readAllByUserId(Integer userId) {

            // Bruger JOIN til at joine projekt-ID'et fra mellemtabellen til projekt-ID'et fra
            // projekt-tabellen, hvor brugerID matcher

            String sql = """
                    SELECT
                        project.id,
                        project.estimatedTime,
                        project.timeSpent,
                        project.name,
                        project.description,
                        project.deadline,
                        project.status
                    FROM project
                    JOIN users_project ON project.id = users_project.projectID
                    WHERE users_project.userID = ?
                    """;

            return jdbcTemplate.query(sql, new ProjectRowMapper(),userId);

        }

        //---------------------------------- Assign User --------------------------------

        // ----------------- Projekt tildeles en bruger efter oprettelse -----------------

        @Override
        public void assignUserToProject(Integer userId, Integer projectId) {

            String sql = "INSERT INTO users_project (userID, projectID) VALUES (?, ?)";

            jdbcTemplate.update(sql, userId, projectId);

        }

    }
