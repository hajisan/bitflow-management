package com.example.estimationtool.repository;

import com.example.estimationtool.interfaces.IRepository;
import com.example.estimationtool.interfaces.ISubProjectRepository;
import com.example.estimationtool.subProject.SubProject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
public class SubProjectRepository implements ISubProjectRepository{

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
            ps.setString(1, subProject.getName());
            ps.setString(2, subProject.getDescription());
            ps.setString(3, subProject.getDeadline().format(DateTimeFormatter.ISO_LOCAL_DATE));
            ps.setString(4, Integer.toString(subProject.getEstimatedTime()));
            ps.setString(5, Integer.toString(0));
            ps.setString(6, subProject.getStatus().name());

            return ps;
        }, keyHolder);

        // Tjekker om ID'et er null og hvis det er sættes det til -1
        int id = keyHolder.getKey() != null ? keyHolder.getKey().intValue() : -1;
        // Sætter subprojektets ID til keyHolderens næste værdi, hvis den ikke er -1
        if (id != -1) subProject.setSubProjectId(id);
        // Returnerer subprojekt-instansen så den kan sendes videre
        return subProject;
    }

    //------------------------------------ Read() ------------------------------------

    @Override
    public List<SubProject> readAll() {


        return List.of();
    }

    @Override
    public SubProject readById(int id) {
        return null;
    }

    //------------------------------------ Update() ------------------------------------

    @Override
    public SubProject update(SubProject subProject) {
        return null;
    }

    //------------------------------------ Delete() ------------------------------------

    @Override
    public void deleteById(int id) {

    }
}
