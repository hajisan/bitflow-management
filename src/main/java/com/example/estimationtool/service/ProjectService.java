package com.example.estimationtool.service;

import com.example.estimationtool.model.enums.Status;
import com.example.estimationtool.repository.interfaces.IProjectRepository;
import com.example.estimationtool.model.Project;
import com.example.estimationtool.toolbox.dto.UserViewDTO;
import com.example.estimationtool.toolbox.roleCheck.RoleCheck;
import org.springframework.data.projection.EntityProjection;
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
        if (project.getDeadLine() == null) {
            throw new IllegalArgumentException("Projektets deadline må ikke være null/tomt");
        }
        if (project.getStatus() == null) {
            throw new IllegalArgumentException("Projektet skal have tildelt en gyldig status");
        }

        return iProjectRepository.create(project);
    }

    //------------------------------------ Read() ------------------------------------

    public List<Project> readAll() {
        return iProjectRepository.readAll();
    }

    public Project readyById(int id) {
        Project project = iProjectRepository.readById(id);
        if (project == null) throw new NoSuchElementException("Projekt med ID" + id + " eksistere ikke.");

        return project;
    }

    //------------------------------------ Delete() ------------------------------------


}
