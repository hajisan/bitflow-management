package com.example.estimationtool.service;

import com.example.estimationtool.model.Task;
import com.example.estimationtool.model.User;
import com.example.estimationtool.model.enums.Status;
import com.example.estimationtool.repository.interfaces.IProjectRepository;
import com.example.estimationtool.repository.interfaces.ISubProjectRepository;
import com.example.estimationtool.model.SubProject;
import com.example.estimationtool.repository.interfaces.ITaskRepository;
import com.example.estimationtool.repository.interfaces.IUserRepository;
import com.example.estimationtool.toolbox.check.StatusCheck;
import com.example.estimationtool.toolbox.dto.SubProjectWithTasksDTO;
import com.example.estimationtool.toolbox.dto.SubProjectWithUsersDTO;
import com.example.estimationtool.toolbox.dto.UserViewDTO;
import com.example.estimationtool.toolbox.check.RoleCheck;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SubProjectService {
    private final ISubProjectRepository iSubProjectRepository;
    private final IUserRepository iUserRepository;
    private final ITaskRepository iTaskRepository;
    private final StatusCheck statusCheck;

    public SubProjectService(ISubProjectRepository iSubProjectRepository, IUserRepository iUserRepository, ITaskRepository iTaskRepository, StatusCheck statusCheck) {
        this.iSubProjectRepository = iSubProjectRepository;
        this.iUserRepository = iUserRepository;
        this.iTaskRepository = iTaskRepository;
        this.statusCheck = statusCheck;
    }

    //------------------------------------ Create() ------------------------------------
    public SubProject create(UserViewDTO currentUser, SubProject subProject) {

        // Kun admin og projektleder må oprette et subprojekt
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


    public SubProject readById(int id) {

        SubProject subProject = iSubProjectRepository.readById(id);
        if (subProject == null) throw new NoSuchElementException("Subprojekt med ID" + id + " eksisterer ikke.");

        return subProject;
    }

    //------------------------------------ Update() ------------------------------------
    public SubProject update(UserViewDTO currentUser, SubProject subProject) {

        // Kun admin eller projektleder må redigere et subprojekt
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
        if (subProject.getStatus() != Status.ACTIVE &&
                subProject.getStatus() != Status.INACTIVE &&
                subProject.getStatus() != Status.DONE) {
            throw new IllegalArgumentException("Subprojektets status skal være sat til enten Active, Inactive eller Done.");
        }

        // Statusvalidering: SubProject må kun sættes til DONE, hvis alle Tasks er DONE
        if (subProject.getStatus() == Status.DONE) {
            List<Task> tasks = iTaskRepository.readAllBySubProjectId(subProject.getSubProjectId());
            // Konverterer SubProject + Task's til DTO
            SubProjectWithTasksDTO dto = new SubProjectWithTasksDTO(subProject, tasks);

            if (!statusCheck.canMarkSubProjectAsDone(dto)) {
                throw new IllegalStateException("Subprojektet kan ikke markeres som færdigt, før alle tasks er færdige.");
            }
        }

        return iSubProjectRepository.update(subProject);
    }

    //------------------------------------ Delete() ------------------------------------

    public void deleteById(UserViewDTO currentUser, int id) {

        // Kun admin eller projektleder må slette et subprojekt
        RoleCheck.ensureAdminOrProjectManager(currentUser.getRole());
        if (id <= 0) {
            throw new IllegalArgumentException("Subprojektet med id " + id + " findes ikke, da id er 0 eller negativt.");
        } else {
            iSubProjectRepository.deleteById(id);
        }
    }

    //---------------------------------- Til DTO'er -----------------------------------

    // --- Henter SubProjekter ud fra brugerID for UserService ---
    public List<SubProject> readAllSubProjectsByUserId(int userId) {

        return iSubProjectRepository.readAllByUserId(userId);
    }


    //------------------------------------ DTO-Mappings -------------------------------


    // --- Henter brugere ud fra subprojektID ---
    public SubProjectWithUsersDTO readAllUsersBySubProjectId(int subProjectId) {

        // Læser ét subprojekt
        SubProject subProject = iSubProjectRepository.readById(subProjectId);

        // Læser listen af brugere ud fra subprojektID
        List<User> userList = iUserRepository.readAllBySubProjectId(subProjectId);

        // Opretter liste af UserViewDTO
        List<UserViewDTO> userViewDTOList = new ArrayList<>();

        // Konverterer userList til UserViewDTOList ved at loope igennem userList
        for (User user : userList) {
            UserViewDTO userViewDTO = new UserViewDTO(
                    user.getUserId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getRole()
            );
            userViewDTOList.add(userViewDTO); // Tilføjer hver UserDTO til listen
        }

        // Returnerer subprojekt + liste af UserViewDTO
        return new SubProjectWithUsersDTO(subProject, userViewDTOList);
    }


    // --- Henter tasks ud fra subprojektID ---
    public SubProjectWithTasksDTO readAllTasksBySubProjectId(int subProjectId) {

        // Læser subprojekt
        SubProject subProject = iSubProjectRepository.readById(subProjectId);

        // Læser tilknyttede tasks
        List<Task> taskList = iTaskRepository.readAllBySubProjectId(subProjectId);

        // Returnerer subprojekt + tasks som én samlet DTO
        return new SubProjectWithTasksDTO(subProject, taskList);
    }


    //---------------------------------- Assign User ---------------------------------


    // ---------------------- Viser kun ikke-tilknyttede brugere ---------------------

    public List<UserViewDTO> readAllUnAssignedUsers(int subProjectId) {

        // Læser alle brugere
        List<User> allUserList = iUserRepository.readAll();

        // Læser subprojektets allerede tilknyttede brugere
        List<User> assignedUserList = iUserRepository.readAllBySubProjectId(subProjectId);

        // Samler ID'er på de allerede tildelte brugere
        Set<Integer> assignedUserIds = new HashSet<>();
        for (User user : assignedUserList) {
            assignedUserIds.add(user.getUserId());
        }

        // Tilføjer kun de brugere, der IKKE allerede er tildelt subprojektet
        List<UserViewDTO> unassignedUserDTO = new ArrayList<>();
        for (User user : allUserList) {
            if (!assignedUserIds.contains(user.getUserId())) {
                unassignedUserDTO.add(new UserViewDTO(
                        user.getUserId(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.getRole()
                ));
            }
        }

        return unassignedUserDTO;
    }

    // ----------------- Subprojekt tildeles en bruger efter oprettelse ---------------

    public void assignUsersToSubProject(UserViewDTO currentUser, List<Integer> userIds, int subProjectId) {

        // Kun admin og projektleder må assign bruger til subprojekt
        RoleCheck.ensureAdminOrProjectManager(currentUser.getRole());

        // Henter de brugere, der allerede er tilknyttet subprojektet
        List<User> existingUsers = iUserRepository.readAllBySubProjectId(subProjectId);

        // Opretter et tomt Set af brugerID'er
        Set<Integer> existingUserIds = new HashSet<>();

        // BrugerID'er gemmes i Settet (undgår duplikater)
        for (User user : existingUsers) {
            existingUserIds.add(user.getUserId());
        }

        // Tjekker om brugerID'et allerede ligger i databasen
        for (Integer userId : userIds) {
            if (!existingUserIds.contains(userId)) {
                iSubProjectRepository.assignUserToSubProject(userId, subProjectId);
            }
        }
    }

}
