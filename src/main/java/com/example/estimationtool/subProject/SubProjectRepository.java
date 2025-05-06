package com.example.estimationtool.subProject;

import com.example.estimationtool.NotApplicableDatatypeException;
import com.example.estimationtool.enums.Status;
import com.example.estimationtool.interfaces.IRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
public class SubProjectRepository implements IRepository<SubProject, Integer> {

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
            if (subProject.getName() != null) ps.setString(1, subProject.getName());
            else
                throw new NotApplicableDatatypeException("Subprojektets navn er enten ikke angivet eller forkert datatype.");

            if (subProject.getDescription() != null) ps.setString(2, subProject.getDescription());
            else
                throw new NotApplicableDatatypeException("Subprojektets beskrivelse er enten ikke angivet eller forkert datatype.");

            if (subProject.getDeadline() != null)
                ps.setString(3, subProject.getDeadline().format(DateTimeFormatter.ISO_LOCAL_DATE));
            else
                throw new NotApplicableDatatypeException("Subprojektets deadline er enten ikke angivet eller forkert datatype.");

            // TODO Jeg er ikke sikker på, hvad vi skal validere her... Måske er det slet ikke i Repository vi skal errorhandle alt det her?
            ps.setString(4, Integer.toString(subProject.getEstimatedTime())); // Må gerne være 0

            ps.setString(5, Integer.toString(0)); // Et subprojekt oprettes som udgangspunkt med 0 timer brugt på det.

            if (subProject.getStatus() != Status.ACTIVE || subProject.getStatus() != Status.INACTIVE || subProject.getStatus() != Status.DONE)
                ps.setString(6, subProject.getStatus().name());
            else
                throw new NotApplicableDatatypeException("Subprojektets navn er enten ikke angivet eller forkert datatype.");

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
