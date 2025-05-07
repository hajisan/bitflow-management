package com.example.estimationtool.service;

import com.example.estimationtool.model.enums.Status;
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

    public SubProjectService(ISubProjectRepository iSubProjectRepository) {
        this.iSubProjectRepository = iSubProjectRepository;
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
        if (subProject.getStatus() != Status.ACTIVE || subProject.getStatus() != Status.INACTIVE || subProject.getStatus() != Status.DONE) {
            throw new IllegalArgumentException("Subprojektets status skal være sat til enten Active, Inactive eller Done.");
        }

        return iSubProjectRepository.create(subProject);
    }

    //------------------------------------ Read() ------------------------------------
    public List<SubProject> readAll() {
        return iSubProjectRepository.readAll();
    }


//    public List<SubProject> readAllFromProjectID(int projectID) {
//
//    }

    public ProjectWithSubProjectsDTO readAllFromProjectId(int projectId) {
        ProjectWithSubProjectsDTO projectWithSubProjectsDTO = iSubProjectRepository.readAllFromProjectId(projectId);

        return
    }

    public SubProject readById(int id) {
        SubProject subProject = iSubProjectRepository.readById(id);
        if (subProject == null) throw new NoSuchElementException("Subprojekt med ID" + id + " eksisterer ikke.");

        return subProject;
    }

    //------------------------------------ Update() ------------------------------------
    public SubProject update(UserViewDTO currentUser, SubProject subProject) {
        RoleCheck.ensureAdminOrProjectManager(currentUser.getRole());
        return null;
    }

    //------------------------------------ Delete() ------------------------------------
    public void deleteById(UserViewDTO currentUser, int id) {
        RoleCheck.ensureAdminOrProjectManager(currentUser.getRole());
    }
}
