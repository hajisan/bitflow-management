package com.example.estimationtool.rowMappers;

import com.example.estimationtool.subProject.SubProject;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SubProjectRowMapper implements RowMapper<SubProject> {
    @Override
    public SubProject mapRow(ResultSet rs, int rowNum) throws SQLException {
        return null;
    }
}
