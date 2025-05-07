package com.example.estimationtool.integrationsTest.repository;

import com.example.estimationtool.model.Project;
import com.example.estimationtool.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;

import static com.example.estimationtool.model.enums.Status.ACTIVE;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest

@Sql(
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
        scripts = {"classpath:h2init.sql"}
)

public class ProjectRepositoryTest {

    @Autowired
    ProjectRepository projectRepository;

    @Test
    void create_savesNewProjectAndChecksDatabase() {
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

    @Test
    void readAll_returnsAllProjectsFromDatabase() {
        //--------- Arrange ---------
        // Testdata indsættes via h2init.sql (to projekter forventes)

        //--------- Act -------------
        var projects = projectRepository.readAll();

        //--------- Assert ----------
        assertEquals(2, projects.size());
        assertEquals("Test Project 1", projects.get(0).getName());
        assertEquals("Test Project 2", projects.get(1).getName());
    }

    @Test
    void readById_returnsCorrectProjectFromDatabase() {
        // --------- Arrange ---------
        // Vi ved fra h2init.sql at projekt med id 1 eksisterer og hedder "Test Project 1"

        int projectId = 1;

        // --------- Act -------------
        Project project = projectRepository.readById(projectId);

        // --------- Assert ----------
        assertNotNull(project);
        assertEquals(projectId, project.getProjectId());
        assertEquals("Test Project 1", project.getName());
        assertEquals("Et testprojekt 1", project.getDescription());
        assertEquals(100, project.getEstimatedTime());
        assertEquals(0, project.getTimeSpent());
        assertEquals("ACTIVE", project.getStatus().name());
        assertEquals(2, project.getUserId()); // Fra user_project-tabellen i h2init.sql
    }

    @Test
    void readById_returnsNullWhenProjectDoesNotExist() {
        // --------- Arrange ---------
        int nonExistentProjectId = 9999; // Et ID vi med sikkerhed ved ikke findes i h2init.sql

        // --------- Act -------------
        Project result = projectRepository.readById(nonExistentProjectId);

        // --------- Assert ----------
        assertNull(result);
    }

    @Test
    void readById_returnsCorrectProjectData() {
        // --------- Arrange ---------
        Project newProject = new Project();
        newProject.setName("Projekt Integration");
        newProject.setDescription("Test af readById");
        newProject.setDeadLine(LocalDate.of(2025, 12, 31));
        newProject.setEstimatedTime(200);
        newProject.setTimeSpent(50);
        newProject.setStatus(ACTIVE);

        Project savedProject = projectRepository.create(newProject);
        int savedId = savedProject.getProjectId();

        // --------- Act -------------
        Project fetchedProject = projectRepository.readById(savedId);

        // --------- Assert ----------
        assertNotNull(fetchedProject);
        assertEquals("Projekt Integration", fetchedProject.getName());
        assertEquals("Test af readById", fetchedProject.getDescription());
        assertEquals(LocalDate.of(2025, 12, 31), fetchedProject.getDeadLine());
        assertEquals(200, fetchedProject.getEstimatedTime());
        assertEquals(50, fetchedProject.getTimeSpent());
        assertEquals(ACTIVE, fetchedProject.getStatus());
    }
}
