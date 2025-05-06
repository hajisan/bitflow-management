package com.example.estimationtool.integrationsTest.service;

import com.example.estimationtool.model.Project;
import com.example.estimationtool.service.ProjectService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static com.example.estimationtool.enums.Status.ACTIVE;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class ProjectServiceTest {

    @Autowired
    private ProjectService projectService;

    @Test // Tester om der bliver kastet en exception når et projekt bliver oprettet uden et navn [FEJLER LIGE PT]
    void createProject_shouldThrowException_whenNameIsBlank() {
        // Arrange
        Project invalidProject = new Project();
        invalidProject.setName(""); // blank navn
        invalidProject.setDescription("Projekt uden navn");
        invalidProject.setDeadLine(LocalDate.of(2025, 6, 1));
        invalidProject.setEstimatedTime(1000);
        invalidProject.setTimeSpent(0);
        invalidProject.setStatus(ACTIVE);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> projectService.createProject(invalidProject));
    }


}
