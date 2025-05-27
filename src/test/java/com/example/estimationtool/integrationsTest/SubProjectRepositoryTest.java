package com.example.estimationtool.integrationsTest;

import com.example.estimationtool.model.SubProject;
import com.example.estimationtool.model.enums.Status;
import com.example.estimationtool.repository.ProjectRepository;
import com.example.estimationtool.repository.SubProjectRepository;
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

public class SubProjectRepositoryTest {

    @Autowired
    SubProjectRepository subProjectRepository;

    @Test // Tjek om et subprojekt bliver oprettet korrekt og får tildelt et ID
    void create_savesNewSubProjectAndReturnsIt() {
        // Arrange
        int projectID = 1; // Forventer at der et projekt med ID 1, i h2init.sql

        SubProject newSubProject = new SubProject();
        newSubProject.setProjectId(projectID); // Subprojektet bliver koblet til projektet med ID 1
        newSubProject.setName("Nyt subprojekt");
        newSubProject.setDescription("Beskrivelsen til supprojektet");
        newSubProject.setDeadline(LocalDate.of(2025, 5, 5));
        newSubProject.setEstimatedTime(1000);
        newSubProject.setTimeSpent(250);
        newSubProject.setStatus(ACTIVE);

        // Act
        SubProject savedSubProject = subProjectRepository.create(newSubProject);

        // Assert
        assertNotNull(savedSubProject.getProjectId());
        assertNotNull(savedSubProject.getSubProjectId());
        assertEquals("Nyt subprojekt", savedSubProject.getName());
    }

    @Test // Tjek om subprojekt med ID 1 bliver hentet korrekt fra databasen
    void readById_returnsCorrectSubProject() {
        // Arrange
        int subProjectID = 1; // Forventer at der er et subprojekt med ID 1, i h2init.sql

        // Act
        SubProject subProject = subProjectRepository.readById(subProjectID);

        // Assert
        assertNotNull(subProject);
        assertEquals(subProjectID, subProject.getSubProjectId());
    }

    @Test // Tjek om et eksisterende subprojekt bliver opdateret korrekt
    void update_modifiesExistingSubProject() {
        // Arrange
        SubProject subProjectToUpdate = subProjectRepository.readById(1);
        subProjectToUpdate.setName("Updated Backend");
        subProjectToUpdate.setDescription("Updated description");

        // Act
        subProjectRepository.update(subProjectToUpdate);
        SubProject updatedSubProject = subProjectRepository.readById(1);

        // Assert
        assertEquals("Updated Backend", updatedSubProject.getName());
        assertEquals("Updated description", updatedSubProject.getDescription());
    }


    @Test // Tjek om et subprojekt slettes korrekt fra databasen
    void deleteById_removesSubProjectFromDatabase() {
        // Arrange
        int subProjectIdToDelete = 1;

        SubProject existingSubProject = subProjectRepository.readById(subProjectIdToDelete);
        assertNotNull(existingSubProject);

        // Act
        subProjectRepository.deleteById(subProjectIdToDelete);

        // Assert
        SubProject deletedSubProject = null; // Forventer at subprojektet nu er slettet og returnere null
        try {
            deletedSubProject = subProjectRepository.readById(subProjectIdToDelete);
        } catch (Exception ignored) {
            // Ignorere exception der kastes
        }

        assertNull(deletedSubProject);
    }


    @Test // Tjek om en bruger uden tilknytning til et subprojekt kan knyttes til subprojektet og det gemmes i mellemtabel
    void assignUserToSubProject_linksUserToSubProject() {
        // Arrange
        int userId = 4; // Bruger som ikke er tilknyttet subprojektet
        int supProjectId = 1; // ID på et eksisterende subprojekt

        // Act
        subProjectRepository.assignUserToSubProject(userId, supProjectId);

        // Assert
        List<SubProject> userSubProjects = subProjectRepository.readAllByUserId(userId);
        boolean found = userSubProjects.stream().
                anyMatch(p -> p.getSubProjectId() == supProjectId);

        assertTrue(found);
    }

    @Test // Tjek om en brugers tilknytning til subprojekter kan hentes fra databasen
    void readAllByUserId_returnsSubProjectsForUser() {
        // Arrange
        int userId = 3; // Bruger som forventes at være tilknyttet 2 subprojekter med ID 1 og 2

        // Act
        List<SubProject> result = subProjectRepository.readAllByUserId(userId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        System.out.println(result);

    }
}