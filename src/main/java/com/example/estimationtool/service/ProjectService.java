package com.example.estimationtool.service;


import com.example.estimationtool.repository.interfaces.IProjectRepository;
import com.example.estimationtool.model.Project;
import com.example.estimationtool.toolbox.dto.UserViewDTO;
import com.example.estimationtool.toolbox.roleCheck.RoleCheck;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

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
        if (project.getDeadline() == null) {
            throw new IllegalArgumentException("Projektets deadline må ikke være null/tomt");
        }
        if (project.getStatus() == null) {
            throw new IllegalArgumentException("Projektet skal have tildelt en gyldig status");
        }

        return iProjectRepository.create(project);
    }

    //------------------------------------ Read() ------------------------------------

    public List<Project> readAll() {
        // Rollevalideringslogik - Det skal kun være en Admin som kan se alle projekter

        return iProjectRepository.readAll();
    }

    public Project readById(int id) {
        Project project = iProjectRepository.readById(id);
        if (project == null) throw new NoSuchElementException("Projekt med ID" + id + " eksistere ikke.");

        return project;
    }

    public List<Project> readByUserId(int userId) {
        // Rollevalideringslogik - en Project Manager eller Developer skal kun kunne se de projekter de knyttet til

        return iProjectRepository.readByUserId(userId);
    }

    //------------------------------------ Update() ------------------------------------

    public Project updateProject(Project project) {
        return iProjectRepository.update(project);
    }

    //------------------------------------ Delete() ------------------------------------

    public void deleteById(int id) {
        iProjectRepository.deleteById(id);
    }

}
