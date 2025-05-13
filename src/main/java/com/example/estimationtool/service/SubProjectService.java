package com.example.estimationtool.service;

import com.example.estimationtool.model.enums.Status;
import com.example.estimationtool.repository.interfaces.IProjectRepository;
import com.example.estimationtool.repository.interfaces.ISubProjectRepository;
import com.example.estimationtool.model.SubProject;
import com.example.estimationtool.toolbox.dto.ProjectWithSubProjectsDTO;
import com.example.estimationtool.toolbox.dto.UserViewDTO;
import com.example.estimationtool.toolbox.roleCheck.RoleCheck;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class SubProjectService {
    private final ISubProjectRepository iSubProjectRepository;
    private final IProjectRepository iProjectRepository;

    public SubProjectService(ISubProjectRepository iSubProjectRepository, IProjectRepository iProjectRepository) {
        this.iSubProjectRepository = iSubProjectRepository;
        this.iProjectRepository = iProjectRepository;
    }

    //------------------------------------ Create() ------------------------------------
    public SubProject create(UserViewDTO currentUser, SubProject subProject) {
        RoleCheck.ensureAdminOrProjectManager(currentUser.getRole());

        // Inputvalidering
        if (subProject.getName() == null || subProject.getName().isBlank()) {
            throw new IllegalArgumentException("Subprojektets navn må ikke være tomt.");
        }
        if (subProject.getEstimatedTime() <= 0) {
            throw new IllegalArgumentException("Subprojektets estimerede tid må ikke være 0 eller negativ.");
        }
        if (subProject.getDeadline() == null) {
            throw new IllegalArgumentException("Subprojektets deadline må ikke være null eller tom.");
        }
        if (subProject.getStatus() != Status.ACTIVE && subProject.getStatus() != Status.INACTIVE && subProject.getStatus() != Status.DONE) {
            throw new IllegalArgumentException("Subprojektets status skal være sat til enten Active, Inactive eller Done.");
        }

        return iSubProjectRepository.create(subProject);
    }

    //------------------------------------ Read() ------------------------------------
    public List<SubProject> readAll() {
        return iSubProjectRepository.readAll();
    }

    public ProjectWithSubProjectsDTO readAllFromProjectId(int projectId) {
//        try {
        ProjectWithSubProjectsDTO projectWithSubProjectsDTO = new ProjectWithSubProjectsDTO(
                iProjectRepository.readById(projectId),
                iSubProjectRepository.readAllFromProjectId(projectId));

        if (projectWithSubProjectsDTO.subProjectList().isEmpty()) {
            throw new NoSuchElementException("Projekt med ID "
                    + projectId +
                    " har ikke nogen subprojekter.");
        }

        return projectWithSubProjectsDTO;
//        } catch (NullPointerException e) {
//            throw new NullPointerException("Enten findes projekt med ID " + projectId + "ikke, eller også findes subprojekterne ikke.");
//        }
    }

    public SubProject readById(int id) {
        SubProject subProject = iSubProjectRepository.readById(id);
        if (subProject == null) throw new NoSuchElementException("Subprojekt med ID" + id + " eksisterer ikke.");

        return subProject;
    }

    //------------------------------------ Update() ------------------------------------
    public SubProject update(UserViewDTO currentUser, SubProject subProject) {
        RoleCheck.ensureAdminOrProjectManager(currentUser.getRole());

        // Inputvalidering til update
        if (subProject.getName() == null || subProject.getName().isBlank()) {
            throw new IllegalArgumentException("Subprojektets navn må ikke være tomt.");
        }
        if (subProject.getEstimatedTime() <= 0) {
            throw new IllegalArgumentException("Subprojektets estimerede tid må ikke være 0 eller negativ.");
        }
        if (subProject.getDeadline() == null) {
            throw new IllegalArgumentException("Subprojektets deadline må ikke være null eller tom.");
        }
        if (subProject.getStatus() != Status.ACTIVE && subProject.getStatus() != Status.INACTIVE && subProject.getStatus() != Status.DONE) {
            throw new IllegalArgumentException("Subprojektets status skal være sat til enten Active, Inactive eller Done.");
        }

        return iSubProjectRepository.update(subProject);
    }

    //------------------------------------ Delete() ------------------------------------
    public void deleteById(UserViewDTO currentUser, int id) {
        RoleCheck.ensureAdminOrProjectManager(currentUser.getRole());
        if (id <= 0) {
            throw new IllegalArgumentException("Subprojektet med id " + id + " findes ikke, da id er 0 eller negativt.");
        } else {
            iSubProjectRepository.deleteById(id);
        }
    }

    //------------------------------------ DTO'er -------------------------------------

    // --- Henter SubProjekter ud fra brugerID ---
    public List<SubProject> readAllSubProjectsByUserId(int userId) {

        return iSubProjectRepository.readAllByUserId(userId);
    }
}
