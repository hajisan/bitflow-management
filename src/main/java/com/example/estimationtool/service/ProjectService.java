package com.example.estimationtool.service;

import com.example.estimationtool.repository.interfaces.IProjectRepository;
import com.example.estimationtool.model.Project;
import com.example.estimationtool.toolbox.dto.UserViewDTO;
import com.example.estimationtool.toolbox.roleCheck.RoleCheck;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    private final IProjectRepository iProjectRepository;

    public ProjectService(IProjectRepository iProjectRepository) {
        this.iProjectRepository = iProjectRepository;
    }

    //------------------------------------ Create() ------------------------------------

    public Project createProject(UserViewDTO currentUser, Project project) {
        RoleCheck.ensureAdminOrProjectManager(currentUser.getRole());

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
        /* TODO Tænker vi skal sørge for at det har en status, men en tom beskrivelse er vel fin nok.
            Et id bliver jo tildelt når det oprettes i databasen med vores KeyHolder så det findes jo egentlig ikke.
            Det gør vel ikke noget at man sætter et id her, men så skal vi bare overskrive det alligevel i Repo...
         */

        return iProjectRepository.create(project);
    }
}
