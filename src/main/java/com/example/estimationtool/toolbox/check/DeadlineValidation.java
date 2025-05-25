package com.example.estimationtool.toolbox.check;

import com.example.estimationtool.model.Project;
import com.example.estimationtool.repository.interfaces.IProjectRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DeadlineValidation {


    private final IProjectRepository iProjectRepository;


    public DeadlineValidation(IProjectRepository iProjectRepository) {
        this.iProjectRepository = iProjectRepository;
    }


    public void validateSubprojectDeadline(int projectId, LocalDate subprojectDeadline) {

        if (subprojectDeadline == null) {
                throw new IllegalArgumentException("Subproject deadline er ugyldig.");
        }

        Project project = iProjectRepository.readById(projectId);


        if (project.getDeadline() == null) {
            throw new IllegalArgumentException("Det tilknyttede projekt kunne ikke findes.");
        }

            if (subprojectDeadline.isAfter(project.getDeadline())) {
                throw new IllegalArgumentException("Subprojektets deadline kan ikke være efter projektets deadline.");
            }
        }
    }

