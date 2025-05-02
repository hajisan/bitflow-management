package com.example.estimationtool.user;

import com.example.estimationtool.interfaces.IUserRepository;
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
            ps.setString(5, user.getRole().getDisplayName());
            return ps;
        }, keyHolder);

        // Tjekker først om id'et er null eller ej. Hvis det er, så sætter vi id-variablen til -1
        int userId = keyHolder.getKey() != null ? keyHolder.getKey().intValue() : -1;
        // Sætter profilens id til KeyHolderens værdi, hvis den ikke var null
        if (userId != -1) user.setUserId(userId);

        return user;
    }

    //------------------------------------ Read() ------------------------------------

    @Override
    public List<User> readAll() {
        return List.of();
    }

    @Override
    public User readById(int id) {
        return null;
    }


    //------------------------------------ Update() ------------------------------------

    @Override
    public User update(User user) {
        return null;
    }

    //------------------------------------ Delete() ------------------------------------


    @Override
    public void deleteById(int id) {

    }
}
