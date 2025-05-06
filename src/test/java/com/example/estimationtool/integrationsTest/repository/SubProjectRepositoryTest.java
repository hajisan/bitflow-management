package com.example.estimationtool.integrationsTest.repository;

import com.example.estimationtool.model.SubProject;
import com.example.estimationtool.model.enums.Status;
import com.example.estimationtool.repository.SubProjectRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest

@Sql(
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
        scripts = {"classpath:h2init.sql"}
)

public class SubProjectRepositoryTest {

    @Autowired
    SubProjectRepository subProjectRepository;

    @Test
    void create() {
        //--------- Arrange ---------
        int testProjectId = 1;
        int estimatedTime = 10;
        String testName = "Test Sub Project";
        String testDescription = "This is a test sub project";
        LocalDate testDeadline = LocalDate.of(2025, 5, 28);
        Status testStatus = Status.ACTIVE;
        SubProject testSubProject = new SubProject(
                testProjectId,
                estimatedTime,
                testName,
                testDescription,
                testDeadline,
                testStatus
        );

        //--------- Act -------------
        SubProject createdSubProject = subProjectRepository.create(testSubProject);

        //--------- Assert ----------
        assertEquals(testSubProject.getName(), createdSubProject.getName());
    }

    @Test
    void readAll() {
    }

    @Test
    void readById() {
    }

    @Test
    void update() {
    }

    @Test
    void deleteById() {
    }
}