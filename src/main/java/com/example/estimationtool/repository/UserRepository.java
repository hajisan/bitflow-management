package com.example.estimationtool.repository;

import com.example.estimationtool.repository.interfaces.IUserRepository;
import com.example.estimationtool.model.User;
import com.example.estimationtool.toolbox.rowMappers.UserRowMapper;
import org.springframework.dao.EmptyResultDataAccessException;
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

// SKAL FJERNES HVIS EXCEPTION HÅNDTERES I SERVICE OG MED @CONTROLLERADVICE
//        // Tjekker først om id'et er null eller ej. Hvis det er, så sætter vi id-variablen til -1
//        int userId = keyHolder.getKey() != null ? keyHolder.getKey().intValue() : -1;
//        // Sætter profilens id til KeyHolderens værdi, hvis den ikke var null
//        if (userId != -1) user.setUserId(userId);

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
}
