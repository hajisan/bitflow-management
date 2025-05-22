package com.example.estimationtool.integrationsTest.repository;

import com.example.estimationtool.model.Project;
import com.example.estimationtool.model.User;
import com.example.estimationtool.model.enums.Role;
import com.example.estimationtool.repository.ProjectRepository;
import com.example.estimationtool.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest

@Sql(
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
        scripts = {"classpath:h2init.sql"}
)

public class UserRepositoryTest {

    @Autowired

    UserRepository userRepository;
    @Autowired
    private ProjectRepository projectRepository;

    @Test // Tjek om en bruger oprettes korrekt og får et auto-generet ID
    void create_returnsNewUser() {
        // Arrange
        User newUser = new User();
        newUser.setFirstName("Bob");
        newUser.setLastName("Marley");
        newUser.setEmail("bob@marley.dk");
        newUser.setPasswordHash("Roots");
        newUser.setRole(Role.ADMIN);

        // Act
        User savedUser = userRepository.create(newUser);

        // Assert
        assertNotNull(savedUser.getUserId()); // Tjek om brugeren har fået et auto-generet ID
        assertEquals("Bob", savedUser.getFirstName());
        assertEquals("Marley", savedUser.getLastName());
        assertEquals("bob@marley.dk", savedUser.getEmail());
        assertEquals("Roots", savedUser.getPasswordHash());
        assertEquals(Role.ADMIN, savedUser.getRole());

        System.out.println(savedUser);
    }

    @Test // Tjek om en bruger kan hentes korrekt fra databasen
    void readyById_returnsCorrectUser() {
        // Arrange
        int userId = 1; // Forventer at h2init.sql indeholder en TestAdmin bruger med ID 1

        // Act
        User getUser = userRepository.readById(userId);

        // Assert
        assertNotNull(getUser); // Sikrer at bruger findes
        assertEquals(1, getUser.getUserId());
        assertEquals("TestAdmin", getUser.getFirstName());
        assertEquals("1", getUser.getLastName());
        assertEquals("test@1.com", getUser.getEmail());
        assertEquals("$2a$12$WshmMsov744g1iD93L.3tuJkScgrJ4q4VgIYuiZZoKwuNK42/9Tiu", getUser.getPasswordHash());
        assertEquals(Role.ADMIN, getUser.getRole());

        System.out.println(getUser);
    }

    @Test // Tjek om alle bruger kan hentes korrekt fra databasen
    void readAll_returnsAllUsers() {
        // Arrange
        // Vi forventer at 4 brugere fra h2init.sql

        // Act
        List<User> users = userRepository.readAll();

        // Assert
        assertEquals(4, users.size());

        // Tjek første bruger
        User firstUser = users.get(0);
        assertEquals("TestAdmin", firstUser.getFirstName());
        assertEquals("1", firstUser.getLastName());
        assertEquals("test@1.com", firstUser.getEmail());
        assertEquals(Role.ADMIN, firstUser.getRole());

        // Udskriv for visuel verifikation
        users.forEach(System.out::println);
    }

    @Test // Tjek om en bruger kan hentes korrekt via email
    void readByEmail_returnsCorrectUser() {
        // Arrange
        String email = "test@1.com";

        // Act
        User foundUser = userRepository.readByEmail(email);

        // Assert
        assertNotNull(foundUser);
        assertEquals("TestAdmin", foundUser.getFirstName());
        assertEquals("1", foundUser.getLastName());
        assertEquals(Role.ADMIN, foundUser.getRole());
        assertEquals(email, foundUser.getEmail());

        System.out.println(foundUser);
    }

    @Test // Tjek om en eksisterende bruger opdateres korrekt
    void update_updatesExistingUser() {
        // Arrange
        User userToUpdate = userRepository.readById(1);
        userToUpdate.setFirstName("Updated");
        userToUpdate.setLastName("Admin");
        userToUpdate.setEmail("updated@admin.com");

        // Act
        userRepository.update(userToUpdate);
        User updatedUser = userRepository.readById(1);

        // Assert
        assertEquals("Updated", updatedUser.getFirstName());
        assertEquals("Admin", updatedUser.getLastName());
        assertEquals("updated@admin.com", updatedUser.getEmail());

        System.out.println(updatedUser);
    }

    @Test // Tjek om en bruger slettes korrekt fra databasen
    void deleteById_deletesExistingUser() {
        // Arrange
        int userIdToDelete = 1; // Vi forventer at bruger med ID 1 findes i h2init.sql

        // Sikrer at brugeren eksisterer før sletning
        User existingUser = userRepository.readById(userIdToDelete);
        assertNotNull(existingUser);

        // Act
        userRepository.deleteById(userIdToDelete);

        // Assert
        User deletedUser = null; // Forventer at user nu er slettet og returnere null
        try {
            deletedUser = userRepository.readById(userIdToDelete);
        } catch (Exception ignored) {
            // Vi ignorerer fx EmptyResultDataAccessException
        }
    }

    @Test // Tjek om brugere knyttet til et projekt hentes korrekt
    void readAllByProjectId_returnsUsersForProject() {
        // Arrange
        int projectId = 1; // Vi forventer at projekt med ID 1 findes i h2init.sql

        // Act
        List<User> users = userRepository.readAllByProjectId(projectId);

        // Assert
        assertEquals(2, users.size()); // Der burde være 2 bruger koblet på projekt 1
        User user1 = users.get(0);
        User user2 = users.get(1);
        System.out.println(user1);
        System.out.println(user2);
    }

    @Test // Tjek om brugere knyttet til et sub-projekt hentes korrekt
    void readAllBySubProjectId_returnUsersForSubProject() {
        // Arrange
        int subProjectId = 1; // Vi forventer at sub-projekt med ID 1 findes i h2init.sql

        // Act
        List<User> users = userRepository.readAllBySubProjectId(subProjectId);

        // Assert
        assertEquals(2, users.size()); // Der burde være 1 bruger koblet på sub-projekt med ID 1
    }

    @Test // Tjek om brugere knyttet til en task  hentes korrekt
    void readAllByTaskId_returnUsersForTask() {
        // Arrange
        int taskId = 1; // Vi forventer at task med ID 1 findes i h2init.sql

        // Act
        List<User> users = userRepository.readAllByTaskId(taskId);

        // Assert
        assertEquals(1, users.size()); // Der burde være 1 bruger koblet på task med ID 1
    }

    @Test // Tjek om korrekt bruger hentes via subTask ID
    void readUserBySubTaskId_returnsCorrectUser() {
        // Arrange
        int subTaskId = 1; // Vi forventer at sub-task med ID 1 findes i h2init.sql

        // Act
        User user = userRepository.readUserBySubTaskId(subTaskId);

        // Assert
        assertNotNull(user);
        assertEquals(3, user.getUserId());
        assertEquals("TestUser1", user.getFirstName());
        assertEquals("3", user.getLastName());
        assertEquals("test@3.com", user.getEmail());
        assertEquals(Role.DEVELOPER, user.getRole());

        System.out.println(user);
    }
}





