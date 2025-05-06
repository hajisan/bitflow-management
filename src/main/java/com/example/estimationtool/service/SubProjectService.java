package com.example.estimationtool.service;

import com.example.estimationtool.enums.Status;
import com.example.estimationtool.interfaces.ISubProjectRepository;
import com.example.estimationtool.subProject.SubProject;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubProjectService {
    private final ISubProjectRepository iSubProjectRepository;

    public SubProjectService(ISubProjectRepository iSubProjectRepository) {
        this.iSubProjectRepository = iSubProjectRepository;
    }

    //------------------------------------ Create() ------------------------------------
    public SubProject create(SubProject subProject) {
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
        return List.of();
    }

    public SubProject readById(int id) {
        return null;
    }

    //------------------------------------ Update() ------------------------------------
    public SubProject update(SubProject subProject) {
        return null;
    }

    //------------------------------------ Delete() ------------------------------------
    public void deleteById(int id) {

    }

}
