package com.example.estimationtool.integrationsTest.service;

import com.example.estimationtool.model.Project;
import com.example.estimationtool.model.enums.Role;
import com.example.estimationtool.service.ProjectService;
import com.example.estimationtool.toolbox.dto.UserViewDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static com.example.estimationtool.model.enums.Status.ACTIVE;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class ProjectServiceTest {

    @Autowired
    private ProjectService projectService;

    @Test // Tester om der bliver kastet en exception når et projekt bliver oprettet uden et navn [FEJLER LIGE PT]
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
        invalidProject.setDeadLine(LocalDate.of(2025, 6, 1));
        invalidProject.setEstimatedTime(1000);
        invalidProject.setTimeSpent(0);
        invalidProject.setStatus(ACTIVE);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> projectService.createProject(userViewDTO, invalidProject));
    }


}
