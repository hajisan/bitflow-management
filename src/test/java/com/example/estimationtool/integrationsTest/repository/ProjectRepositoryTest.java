package com.example.estimationtool.integrationsTest.repository;

import com.example.estimationtool.model.Project;
import com.example.estimationtool.model.enums.Status;
import com.example.estimationtool.repository.ProjectRepository;
import com.example.estimationtool.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
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

public class ProjectRepositoryTest {

    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    private UserRepository userRepository;

    @Test // Tjek om et projekt bliver oprettet korrekt og får tildelt et ID
    void create_savesNewProjectAndChecksDatabase() {
        // Arrange
        Project newProject = new Project();
        newProject.setName("New Project 1");
        newProject.setDescription("Et nyt projekt 1");
        newProject.setDeadline(LocalDate.of(2025, 5, 5));
        newProject.setEstimatedTime(1000);
        newProject.setTimeSpent(250);
        newProject.setStatus(ACTIVE);

        // Act
        Project savedProject = projectRepository.create(newProject);

        // Assert
        assertNotNull(savedProject.getProjectId());
        assertEquals("New Project 1", savedProject.getName());
    }

    @Test // Tjek om alle projekter bliver hentet korrekt fra databasen
    void readAll_returnsAllProjectsFromDatabase() {
        // Arrange
        // Testdata indsættes via h2init.sql (to projekter forventes)

        // Act
        var projects = projectRepository.readAll();

        // Assert
        assertEquals(2, projects.size());
        assertEquals("Test Project 1", projects.get(0).getName());
        assertEquals("Test Project 2", projects.get(1).getName());
    }

    @Test // Tjek om projekt med ID 1 bliver hentet korrekt fra databasen
    void readById_returnsCorrectProjectFromDatabase() {
        // Arrange
        // Vi ved fra h2init.sql at projekt med id 1 eksisterer og hedder "Test Project 1"

        int projectId = 1;

        // Act
        Project project = projectRepository.readById(projectId);

        // Assert
        assertNotNull(project);
        assertEquals(projectId, project.getProjectId());
        assertEquals("Test Project 1", project.getName());
        assertEquals("Et testprojekt 1", project.getDescription());
        assertEquals(100, project.getEstimatedTime());
        assertEquals(0, project.getTimeSpent());
        assertEquals("ACTIVE", project.getStatus().name());
    }

    @Test // Skal fejle da projektet ikke eksisterer
    void readById_throwsExceptionWhenProjectDoesNotExist() {
        // Arrange
        int nonExistentProjectId = 9999; // Et ID vi med sikkerhed ved ikke findes i h2init.sql

        // Act
        // Vi bruger Executable-interfacet fra JUnits bibliotekt sammen med ForEmptyResultDataAccessException fra Jdbc
        // Executable-interfacet indeholder en enkelt metode: void execute, som skal kaste en throwable
        Executable testForEmptyResultDataAccessException = new Executable() {
            @Override
            public void execute() {
                projectRepository.readById(nonExistentProjectId);
            }
        };

        // Assert
        // Her bruger vi assertThrows med EmptyResultDataAccessException som den forventede exception og
        // testForEmptyResultDataAccessException-executablen, som indeholder den metode der bør kaste vores exception
        assertThrows(EmptyResultDataAccessException.class, testForEmptyResultDataAccessException);
    }

    @Test // Tjek om et projekt bliver hentet korrekt fra databasen
    void readById_returnsCorrectProjectData() {
        // Arrange
        Project newProject = new Project();
        newProject.setName("Projekt Integration");
        newProject.setDescription("Test af readById");
        newProject.setDeadline(LocalDate.of(2025, 12, 31));
        newProject.setEstimatedTime(200);
        newProject.setTimeSpent(50);
        newProject.setStatus(ACTIVE);

        Project savedProject = projectRepository.create(newProject);
        int savedId = savedProject.getProjectId();

        // Act
        Project fetchedProject = projectRepository.readById(savedId);

        // Assert
        assertNotNull(fetchedProject);
        assertEquals("Projekt Integration", fetchedProject.getName());
        assertEquals("Test af readById", fetchedProject.getDescription());
        assertEquals(LocalDate.of(2025, 12, 31), fetchedProject.getDeadline());
        assertEquals(200, fetchedProject.getEstimatedTime());
        assertEquals(50, fetchedProject.getTimeSpent());
        assertEquals(ACTIVE, fetchedProject.getStatus());
    }

    @Test // Tjek om et eksisterende projekt bliver opdateret korrekt
    void update_returnUpdatedProject() {
        // Arrange
        Project projectToUpdate = projectRepository.readById(1);
        projectToUpdate.setName("Updated Test Project 1");
        projectToUpdate.setDescription("Updated description");
        projectToUpdate.setDeadline(LocalDate.of(2025, 8, 8));
        projectToUpdate.setEstimatedTime(2500);
        projectToUpdate.setTimeSpent(2500);
        projectToUpdate.setStatus(Status.DONE);

        // Act
        projectRepository.update(projectToUpdate);
        Project updatedProject = projectRepository.readById(1);

        // Assert
        assertEquals("Updated Test Project 1", updatedProject.getName());
        assertEquals("Updated description", updatedProject.getDescription());
        assertEquals(LocalDate.of(2025, 8, 8), updatedProject.getDeadline());
        assertEquals(2500, updatedProject.getEstimatedTime());
        assertEquals(2500, updatedProject.getTimeSpent());
        assertEquals(Status.DONE, updatedProject.getStatus());
    }

    @Test // Tjek om et projekt slettes korrekt fra databasen
    void deleteById_deletesExistingProject() {
        // Arrange
        int projectIdToDelete = 1;

        // Sikrer at projektet eksisterer før sletning
        Project existingProject = projectRepository.readById(projectIdToDelete);
        assertNotNull(existingProject);

        // Act
        projectRepository.deleteById(projectIdToDelete);

        // Assert
        Project deletedProject = null; // Forventer at projektet nu er slettet og returnerer null
        try {
            deletedProject = projectRepository.readById(projectIdToDelete);
        } catch (Exception ignored) { // Vi ignorerer fx EmptyResultDataAccessException
        }

        assertNull(deletedProject);
    }

    @Test // Tjek om projekter tilknyttet en bruger hentes korrekt fra databasen
    void readAllByUserId_returnProjectForUserID() {
        // Arrange
        int userID = 2;

        // Act
        List<Project> projects = projectRepository.readAllByUserId(userID);

        // Assert
        assertEquals(1, projects.size());
        assertEquals("Test Project 1", projects.get(0).getName());

        projects.forEach(System.out::println);
    }

    @Test // Tjek om en bruger kan tildeles et nyt projekt
    void assignUserToProject_assignsUserCorrectly() {
        // --------- Arrange ---------
        int userId = 3; // En udvikler
        Project newProject = new Project();
        newProject.setName("Assignment Test Project");
        newProject.setDescription("Projekt til tildelingstest");
        newProject.setDeadline(LocalDate.of(2025, 12, 31));
        newProject.setEstimatedTime(120);
        newProject.setTimeSpent(0);
        newProject.setStatus(Status.ACTIVE);

        Project savedProject = projectRepository.create(newProject);

        // --------- Act -------------
        projectRepository.assignUserToProject(userId, savedProject.getProjectId());

        // --------- Assert ----------
        List<Project> userProjects = projectRepository.readAllByUserId(userId);

        boolean found = userProjects.stream()
                .anyMatch(p -> p.getProjectId() == savedProject.getProjectId());

        assertTrue(found);
    }

    @Test // Tjek om en bruger uden tilknytning til et projekt kan knyttes til projektet
    void assignUserToExistingProject_worksCorrectly() {
        // Arrange
        int userId = 4; // Bruger er ikke tilknyttet noget projekt
        int existingProjectId = 1; // ID på eksisterende projekt

        // Act
        projectRepository.assignUserToProject(userId, existingProjectId);

        // Assert
        List<Project> userProjects = projectRepository.readAllByUserId(userId);
        boolean found = userProjects.stream()
                .anyMatch(p -> p.getProjectId() == existingProjectId);

        assertTrue(found);
    }

}
