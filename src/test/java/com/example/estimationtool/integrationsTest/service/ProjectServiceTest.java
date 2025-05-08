package com.example.estimationtool.integrationsTest.service;

import com.example.estimationtool.model.Project;
import com.example.estimationtool.model.enums.Role;
import com.example.estimationtool.service.ProjectService;
import com.example.estimationtool.toolbox.dto.UserViewDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;

import static com.example.estimationtool.model.enums.Status.ACTIVE;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest

@Sql(
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
        scripts = {"classpath:h2init.sql"}
)

public class ProjectServiceTest {

    @Autowired
    private ProjectService projectService;

    @Test // Tester om der bliver kastet en exception når et projekt bliver oprettet uden et navn
    void createProject_shouldThrowException_whenNameIsBlank() {
        // Arrange
        UserViewDTO userViewDTO = new UserViewDTO(
                1, // userId
                "John", // firstName
                "Doe", // lastName
                "john.doe@example.com", // email
                Role.ADMIN // role
        );
        Project invalidProject = new Project();
        invalidProject.setName(""); // blank navn
        invalidProject.setDescription("Projekt uden navn");
        invalidProject.setDeadline(LocalDate.of(2025, 6, 1));
        invalidProject.setEstimatedTime(1000);
        invalidProject.setTimeSpent(0);
        invalidProject.setStatus(ACTIVE);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> projectService.createProject(userViewDTO, invalidProject));
    }

    @Test
    void readAll_returnsAllProjectsFromDatabase() {
        // --------- Arrange ---------
        UserViewDTO userViewDTO = new UserViewDTO(
                1, // userId
                "John", // firstName
                "Doe", // lastName
                "john.doe@example.com", // email
                Role.ADMIN // role
        );

        Project newProject1 = new Project();
        newProject1.setName("New Project 1");
        newProject1.setDescription("Et nyt projekt 1");
        newProject1.setDeadline(LocalDate.of(2025, 5, 5));
        newProject1.setEstimatedTime(1000);
        newProject1.setTimeSpent(250);
        newProject1.setStatus(ACTIVE);

        Project newProject2 = new Project();
        newProject2.setName("New Project 2");
        newProject2.setDescription("Et nyt projekt 2");
        newProject2.setDeadline(LocalDate.of(2025, 5, 5));
        newProject2.setEstimatedTime(1000);
        newProject2.setTimeSpent(250);
        newProject2.setStatus(ACTIVE);

        // --------- Act -------------
        projectService.createProject(userViewDTO, newProject1);
        projectService.createProject(userViewDTO, newProject2);

        List<Project> projects = projectService.readAll();

        // --------- Assert ----------
        assertTrue(projects.stream().anyMatch(p -> p.getName().equals("New Project 1")));
        assertTrue(projects.stream().anyMatch(p -> p.getName().equals("New Project 2")));
        assertTrue(projects.size() >= 2); // Hvis databasen allerede har projekter fra H2 init
    }

    @Test
    void readById__ReturnsCorrectProjectData() {
        // --------- Arrange ---------
        UserViewDTO userViewDTO = new UserViewDTO(
                1, // userId
                "John", // firstName
                "Doe", // lastName
                "john.doe@example.com", // email
                Role.ADMIN // role
        );

        Project newProject1 = new Project();
        newProject1.setName("New Project 1");
        newProject1.setDescription("Et nyt projekt 1");
        newProject1.setDeadline(LocalDate.of(2025, 5, 5));
        newProject1.setEstimatedTime(1000);
        newProject1.setTimeSpent(250);
        newProject1.setStatus(ACTIVE);

        // --------- Act -------------
        Project savedProject = projectService.createProject(userViewDTO, newProject1);
        int savedId = savedProject.getProjectId(); // <-- korrekt ID fra databasen

        // --------- Assert ----------
        Project fetchedProject = projectService.readById(savedId);
        assertNotNull(fetchedProject);
        assertEquals("New Project 1", fetchedProject.getName());
        assertEquals("Et nyt projekt 1", fetchedProject.getDescription());
        assertEquals(LocalDate.of(2025, 5, 5), fetchedProject.getDeadline());
        assertEquals(1000, fetchedProject.getEstimatedTime());
        assertEquals(250, fetchedProject.getTimeSpent());
        assertEquals(ACTIVE, fetchedProject.getStatus());

    }




}
