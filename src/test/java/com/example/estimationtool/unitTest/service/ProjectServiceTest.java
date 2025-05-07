package com.example.estimationtool.unitTest.service;

import com.example.estimationtool.model.enums.Role;
import com.example.estimationtool.repository.interfaces.IProjectRepository;
import com.example.estimationtool.model.Project;
import com.example.estimationtool.service.ProjectService;
import com.example.estimationtool.toolbox.dto.UserViewDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static com.example.estimationtool.model.enums.Status.ACTIVE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // For at kunne bruge @Mock og @InjectMock
public class ProjectServiceTest {

    @Mock
    private IProjectRepository iProjectRepository;

    @InjectMocks
    private ProjectService projectService;

    private Project testProject;

    @BeforeEach
    void setUp() {
        testProject = new Project();
        testProject.setName("New Project");
        testProject.setDescription("New Project Description");
        testProject.setDeadLine(LocalDate.of(2025, 5, 5));
        testProject.setEstimatedTime(1000);
        testProject.setTimeSpent(250);
        testProject.setStatus(ACTIVE);
    }

    @Test // Tjekker om projekt bliver opretter
    void test_projectCreate() {
        //--------- Arrange ---------
        UserViewDTO userViewDTO = new UserViewDTO(
                1, // userId
                "John", // firstName
                "Doe", // lastName
                "john.doe@example.com", // email
                Role.ADMIN // role
        );
        when(iProjectRepository.create(testProject)).thenReturn(testProject);

        //--------- Act -------------
        Project result = projectService.createProject(userViewDTO, testProject);

        //--------- Assert ----------
        assertEquals(testProject, result);
        verify(iProjectRepository).create(testProject);

    }

}
