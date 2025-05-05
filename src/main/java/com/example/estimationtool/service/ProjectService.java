package com.example.estimationtool.service;

import com.example.estimationtool.interfaces.IProjectRepository;
import com.example.estimationtool.model.Project;

public class ProjectService {

    private final IProjectRepository iProjectRepository;

    public ProjectService(IProjectRepository iProjectRepository) {
        this.iProjectRepository = iProjectRepository;
    }

    //------------------------------------ Create() ------------------------------------

    public Project createProject(Project project) {
        // Inputvalidering
        if (project.getName() == null || project.getName().isBlank()) {
            throw new IllegalArgumentException("Projektets navn må ikke være tomt");
        }
        if (project.getEstimatedTime() <= 0) {
            throw new IllegalArgumentException("Projektets estimeret tid må ikke være 0 eller negativ");
        }
        if (project.getDeadLine() == null) {
            throw new IllegalArgumentException("Projektets deadline må ikke være null/tomt");
        }

        /* Bør man validere alle input?
        - at projektet har et id?
        - at projektet har en beskrivelse?
        - at projektet har en status?
         */

        return iProjectRepository.create(project);
    }
}
