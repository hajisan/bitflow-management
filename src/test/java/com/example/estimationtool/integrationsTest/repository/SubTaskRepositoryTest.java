package com.example.estimationtool.integrationsTest.repository;

import com.example.estimationtool.model.SubTask;
import com.example.estimationtool.repository.SubTaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;

import static com.example.estimationtool.model.enums.Status.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql(
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
        scripts = {"classpath:h2init.sql"}
)
public class SubTaskRepositoryTest {

    @Autowired
    SubTaskRepository subTaskRepository;

    @Test // Tjek om en subtask bliver oprettet korrekt og får tildelt et ID
    void create_savesNewSubTaskAndReturnsIt() {
        // Arrange
        SubTask subTask = new SubTask();
        subTask.setTaskId(1);
        subTask.setName("Test subtask");
        subTask.setDescription("Subtask description");
        subTask.setDeadline(LocalDate.of(2025, 6, 10));
        subTask.setEstimatedTime(300);
        subTask.setTimeSpent(50);
        subTask.setStatus(ACTIVE);

        // Act
        SubTask saved = subTaskRepository.create(subTask);

        // Assert
        assertNotNull(saved.getSubTaskId());
        assertEquals("Test subtask", saved.getName());
    }

    @Test // Tjek om en subtask bliver hentet korrekt fra databasen
    void readById_returnsCorrectSubTask() {
        // Arrange
        int subTaskId = 1;

        // Act
        SubTask subTask = subTaskRepository.readById(subTaskId);

        // Assert
        assertNotNull(subTask);
        assertEquals(subTaskId, subTask.getSubTaskId());
    }

    @Test // Tjek om en eksisterende subtask bliver opdateret korrekt
    void update_modifiesExistingSubTask() {
        // Arrange
        SubTask subTask = subTaskRepository.readById(1);
        subTask.setName("Updated name");
        subTask.setDescription("Updated desc");

        // Act
        subTaskRepository.update(subTask);
        SubTask updated = subTaskRepository.readById(1);

        // Assert
        assertEquals("Updated name", updated.getName());
        assertEquals("Updated desc", updated.getDescription());
    }

    @Test // Tjek om en subtask bliver slettet korrekt fra databasen
    void deleteById_removesSubTaskFromDatabase() {
        // Arrange
        SubTask existing = subTaskRepository.readById(1);
        assertNotNull(existing);

        // Act
        subTaskRepository.deleteById(1);

        // Assert
        SubTask deleted = null;
        try {
            deleted = subTaskRepository.readById(1);
        } catch (Exception ignored) {
        }
        assertNull(deleted);
    }

    @Test // Tjek om en bruger kan tildeles en subtask og det gemmes korrekt i mellemtabel
    void assignUserToSubTask_linksUserToSubTask() {
        // Arrange
        int userId = 4;
        int subTaskId = 1;

        // Act
        subTaskRepository.assignUserToSubTask(userId, subTaskId);

        // Assert
        List<SubTask> userSubTasks = subTaskRepository.readAllByUserId(userId);
        boolean found = userSubTasks.stream().anyMatch(t -> t.getSubTaskId() == subTaskId);
        assertTrue(found);
    }

    @Test // Tjek om subtasks tilknyttet en bruger kan hentes korrekt
    void readAllByUserId_returnsSubTasksForUser() {
        // Arrange
        int userId = 3;

        // Act
        List<SubTask> subTasks = subTaskRepository.readAllByUserId(userId);

        // Assert
        assertNotNull(subTasks);
        assertEquals(4, subTasks.size());
    }

    @Test // Tjek om subtasks tilknyttet en task kan hentes korrekt
    void readAllByTaskId_returnsSubTasksForTask() {
        // Arrange
        int taskId = 1;

        // Act
        List<SubTask> subTasks = subTaskRepository.readAllByTaskId(taskId);

        // Assert
        assertNotNull(subTasks);
        assertTrue(subTasks.size() >= 1);
    }
}
