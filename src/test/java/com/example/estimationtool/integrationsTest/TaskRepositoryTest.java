package com.example.estimationtool.integrationsTest;

import com.example.estimationtool.model.Task;
import com.example.estimationtool.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static com.example.estimationtool.model.enums.Status.*;

@SpringBootTest
@Sql(
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
        scripts = {"classpath:h2init.sql"}
)
public class TaskRepositoryTest {

    @Autowired
    TaskRepository taskRepository;

    @Test // Tjek om en Task bliver oprettet korrekt og får tildelt et ID
    void create_savesNewTaskAndReturnsIt() {
        // Arrange
        Task task = new Task();
        task.setSubProjectId(1); // Task knyttes til subprojekt med ID 1
        task.setName("Test task");
        task.setDescription("Task description");
        task.setDeadline(LocalDate.of(2025, 6, 1));
        task.setEstimatedTime(500);
        task.setTimeSpent(100);
        task.setStatus(ACTIVE);

        // Act
        Task saved = taskRepository.create(task);

        // Assert
        assertNotNull(saved.getTaskId());
        assertEquals("Test task", saved.getName());
    }

    @Test // Tjek om en task bliver hentet korrekt fra databasen
    void readById_returnsCorrectTask() {
        // Arrange
        int taskId = 1; // Task med dette ID skal findes i h2init.sql

        // Act
        Task task = taskRepository.readById(taskId);

        // Assert
        assertNotNull(task);
        assertEquals(taskId, task.getTaskId());
    }

    @Test // Tjek om en eksisterende task bliver opdateret korrekt
    void update_modifiesExistingTask() {
        // Arrange
        Task task = taskRepository.readById(1);
        task.setName("Updated name");
        task.setDescription("Updated desc");

        // Act
        taskRepository.update(task);
        Task updated = taskRepository.readById(1);

        // Assert
        assertEquals("Updated name", updated.getName());
        assertEquals("Updated desc", updated.getDescription());
    }

    @Test // Tjek om en task bliver slettet korrekt fra databasen
    void deleteById_removesTaskFromDatabase() {
        // Arrange
        Task existing = taskRepository.readById(1);
        assertNotNull(existing); // Sikrer at task findes inden sletning

        // Act
        taskRepository.deleteById(1);

        // Assert
        Task deleted = null;
        try {
            deleted = taskRepository.readById(1); // Forsøger at hente den slettede task
        } catch (Exception ignored) {
            // Forventer exception eller null
        }
        assertNull(deleted);
    }

    @Test // Tjek om en bruger kan tildeles en task og det gemmes korrekt
    void assignUserToTask_linksUserToTask() {
        // Arrange
        int userId = 4; // Bruger uden tilknytning til task
        int taskId = 1;

        // Act
        taskRepository.assignUserToTask(userId, taskId);

        // Assert
        List<Task> userTasks = taskRepository.readAllByUserId(userId);
        boolean found = userTasks.stream().anyMatch(t -> t.getTaskId() == taskId);
        assertTrue(found); // Brugerens tasks skal indeholde den pågældende task
    }

    @Test // Tjek om tasks tilknyttet en bruger kan hentes korrekt
    void readAllByUserId_returnsTasksForUser() {
        // Arrange
        int userId = 3; // Bruger tilknyttet 2 tasks ifølge h2init.sql

        // Act
        List<Task> tasks = taskRepository.readAllByUserId(userId);

        // Assert
        assertNotNull(tasks);
        assertEquals(2, tasks.size());
    }

    @Test // Tjek om tasks tilknyttet et subprojekt kan hentes korrekt
    void readAllBySubProjectId_returnsTasksForSubProject() {
        // Arrange
        int subProjectId = 1; // Forventer at dette subprojekt har mindst én task

        // Act
        List<Task> tasks = taskRepository.readAllBySubProjectId(subProjectId);

        // Assert
        assertNotNull(tasks);
        assertTrue(tasks.size() >= 1);
    }
}