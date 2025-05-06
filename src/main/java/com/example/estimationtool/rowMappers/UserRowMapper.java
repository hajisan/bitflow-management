package com.example.estimationtool.rowMappers;

import com.example.estimationtool.enums.Role;
import com.example.estimationtool.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("firstName"),
                rs.getString("lastName"),
                rs.getString("email"),
                rs.getString("passwordHash"),
                Role.valueOf(rs.getString("role"))
        );
    }
}
