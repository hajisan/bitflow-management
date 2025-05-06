package com.example.estimationtool.integrationsTest.repository;

import com.example.estimationtool.model.Project;
import com.example.estimationtool.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;

import static com.example.estimationtool.enums.Status.ACTIVE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest

@Sql(
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
        scripts = {"classpath:h2init.sql"}
)

public class ProjectRepositoryTest {

    @Autowired
    ProjectRepository projectRepository;

    @Test // Tester oprettelsen af et nyt projekt og om det bliver gemt i databasen
    void create() {
        //--------- Arrange ---------
        Project newProject = new Project();
        newProject.setName("New Project 1");
        newProject.setDescription("Et nyt projekt 1");
        newProject.setDeadLine(LocalDate.of(2025, 5, 5));
        newProject.setEstimatedTime(1000);
        newProject.setTimeSpent(250);
        newProject.setStatus(ACTIVE);

        //--------- Act -------------
        Project savedProject = projectRepository.create(newProject);

        //--------- Assert ----------
        assertNotNull(savedProject.getProjectId());
        assertEquals("New Project 1", savedProject.getName());
    }

    @Test // Tester om alle projekter kan hentes fra h2init.sql
    void readAll() {
        //--------- Arrange ---------
        // Testdata indsættes via h2init.sql (to projekter forventes)

        //--------- Act -------------
        var projects = projectRepository.readAll();

        //--------- Assert ----------
        assertEquals(2, projects.size());
        assertEquals("Test Project 1", projects.get(0).getName());
        assertEquals("Test Project 2", projects.get(1).getName());

    }

    //--------- Arrange ---------
    //--------- Act -------------
    //--------- Assert ----------

    //--------- Arrange ---------
    //--------- Act -------------
    //--------- Assert ----------
}
