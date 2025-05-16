package com.example.estimationtool.repository;

import com.example.estimationtool.repository.interfaces.IUserRepository;
import com.example.estimationtool.model.User;
import com.example.estimationtool.toolbox.rowMappers.UserRowMapper;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class UserRepository implements IUserRepository {

    private final JdbcTemplate jdbcTemplate;

    // Injection af JdbcTemplate til databasekald
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //------------------------------------ Create() ------------------------------------


    @Override
    public User create(User user) {

        String sql = "INSERT INTO user (firstName, lastName, email, passwordHash, role) VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        // Bruger PreparedStatement sammen med vores GeneratedKeyHolder til at kunne autogenerere et nyt id
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPasswordHash());
            ps.setString(5, user.getRole().name());
            return ps;
        }, keyHolder);

        int generatedId = keyHolder.getKey().intValue();
        user.setUserId(generatedId);  // Sætter ID på User

        return user;
    }

    //------------------------------------ Read() ------------------------------------

    @Override
    public List<User> readAll() {

        String sql = "SELECT id, firstName, lastName, email, passwordHash, role FROM user";
        return jdbcTemplate.query(sql, new UserRowMapper());
    }

    @Override
    public User readById(Integer id) {

        String sql = "SELECT id, firstName, lastName, email, passwordHash, role FROM user WHERE id = ?";

            return jdbcTemplate.queryForObject(sql, new UserRowMapper(), id);


    }


    //------------------------------------ Update() ------------------------------------

    @Override
    public User update(User user) {

        String sql = "UPDATE user SET firstName = ?, lastName = ?, email = ?, passwordHash = ?, role = ? WHERE id = ?";


            jdbcTemplate.update( // Henter disse værdier, så de kan opdateres
                    sql,
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getPasswordHash(),
                    user.getRole().name(), // Konverteres til String for at gemmes i databasen
                    user.getUserId() // Parameter -> id til WHERE
            );
            return user;
        }

    //------------------------------------ Delete() ------------------------------------


    @Override
    public void deleteById(Integer id) {

        String sql = "DELETE FROM user WHERE id = ?";
        jdbcTemplate.update(sql, id);

    }

    //------------------------------------ Login() -------------------------------------

    @Override
    public User readByEmail(String email) {
        String sql = """
            SELECT id, firstName, lastName, email, passwordHash, role
            FROM user
            WHERE email = ?
            """;

            return jdbcTemplate.queryForObject(sql, new UserRowMapper(), email);

    }

    //---------------------------------- Til DTO'er ------------------------------------

    // --- Read() brugere ud fra projekt-ID ---

    @Override
    public List<User> readAllByProjectId(Integer projectId) {

        // Bruger JOIN til at joine bruger-ID'et fra mellemtabellen til bruger-ID'et fra
        // user-tabellen, hvor projektID matcher

        String sql = """
                SELECT
                    user.id,
                    user.firstName,
                    user.lastName,
                    user.email,
                    user.passwordHash,
                    user.role
                FROM user
                JOIN user_project ON user.id = user_project.userID
                WHERE user_project.projectID = ?
                """;

            return jdbcTemplate.query(sql, new UserRowMapper(), projectId);

    }

    // --- Read() brugere ud fra subprojekt-ID ---

    @Override
    public List<User> readAllBySubProjectId(Integer subProjectId) {

        // Bruger JOIN til at joine bruger-ID'et fra mellemtabellen til bruger-ID'et fra
        // user-tabellen, hvor subprojectID matcher

        String sql = """
                SELECT
                    user.id,
                    user.firstName,
                    user.lastName,
                    user.email,
                    user.passwordHash,
                    user.role
                FROM user
                JOIN user_subproject ON user.id = user_subproject.userID
                WHERE user_subproject.subProjectID = ?
                """;

            return jdbcTemplate.query(sql, new UserRowMapper(), subProjectId);

    }

    // --- Read() brugere ud fra task-ID ---

    @Override
    public List<User> readAllByTaskId(Integer taskId) {

        // Bruger JOIN til at joine bruger-ID'et fra mellemtabellen til bruger-ID'et fra
        // bruger-tabellen, hvor brugerID matcher

        String sql = """
                SELECT
                    user.id,
                    user.firstName,
                    user.lastName,
                    user.email,
                    user.passwordHash,
                    user.role
                FROM user
                JOIN user_task ON user.id = user_task.userID
                WHERE user_task.taskID = ?
                """;

            return jdbcTemplate.query(sql, new UserRowMapper(), taskId);

    }


    // Read() bruger ud fra subtask-ID (fordi der er en mange-til-mange relation i databasen)

    @Override
    public User readUserBySubTaskId(Integer subTaskId) {

        String sql = """
        SELECT
            user.id,
            user.firstName,
            user.lastName,
            user.email,
            user.passwordHash,
            user.role
        FROM user
        JOIN user_subtask ON user.id = user_subtask.userID
        WHERE user_subtask.subTaskID = ?
    """;

        List<User> users = jdbcTemplate.query(sql, new UserRowMapper(), subTaskId);

        if (users.isEmpty()) {
            return null;
        } else {
            return users.get(0); // Returnerer den første (og eneste) bruger tilknyttet
        }
    }


}
