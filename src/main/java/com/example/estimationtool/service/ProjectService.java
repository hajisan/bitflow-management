package com.example.estimationtool.service;


import com.example.estimationtool.model.SubProject;
import com.example.estimationtool.model.User;
import com.example.estimationtool.model.enums.Status;
import com.example.estimationtool.repository.interfaces.IProjectRepository;
import com.example.estimationtool.model.Project;
import com.example.estimationtool.repository.interfaces.ISubProjectRepository;
import com.example.estimationtool.repository.interfaces.IUserRepository;
import com.example.estimationtool.toolbox.check.AssignCheck;
import com.example.estimationtool.toolbox.check.DeadlineCheck;
import com.example.estimationtool.toolbox.check.StatusCheck;
import com.example.estimationtool.toolbox.dto.ProjectWithSubProjectsDTO;
import com.example.estimationtool.toolbox.dto.ProjectWithUsersDTO;
import com.example.estimationtool.toolbox.dto.UserViewDTO;
import com.example.estimationtool.toolbox.check.RoleCheck;
import com.example.estimationtool.toolbox.dto.UserWithProjectsDTO;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProjectService {

    private final IProjectRepository iProjectRepository;
    private final IUserRepository iUserRepository;
    private final ISubProjectRepository iSubProjectRepository;
    private final StatusCheck statusCheck;

    public ProjectService(IProjectRepository iProjectRepository, IUserRepository iUserRepository, ISubProjectRepository iSubProjectRepository, StatusCheck statusCheck) {
        this.iProjectRepository = iProjectRepository;
        this.iUserRepository = iUserRepository; //Til at knytte projekt til brugere
        this.iSubProjectRepository = iSubProjectRepository;
        this.statusCheck = statusCheck;
    }

    //------------------------------------ Create() ------------------------------------

    public Project createProject(UserViewDTO currentUser, Project project) {

        // Kun admin og projektleder må oprette et projekt
        RoleCheck.ensureAdminOrProjectManager(currentUser.getRole());

        Project projectWithUser = iProjectRepository.create(project);

        // TODO - OBS! Projekt tildeles bruger ved oprettelse. Det er et forretningsvalg.
        iProjectRepository.assignUserToProject(currentUser.getUserId(), project.getProjectId());

        return projectWithUser;
    }


    //------------------------------------ Read() ------------------------------------

    public List<Project> readAll(UserViewDTO currentUser) {

        // Kun admin må se alle projekter
        //RoleCheck.ensureAdmin(currentUser.getRole());

        return iProjectRepository.readAll();
    }

    public Project readById(int id) {

        Project project = iProjectRepository.readById(id);
        if (project == null) throw new NoSuchElementException("Projekt med ID" + id + " eksisterer ikke.");

        return project;
    }



    //------------------------------------ Update() ------------------------------------

    public Project updateProject(UserViewDTO currentUser, Project project) {

        // Kun admin eller projektleder må redigere projekt
        RoleCheck.ensureAdminOrProjectManager(currentUser.getRole());

        // Assign-tjek: tjekker om brugeren er tildelt et projekt
        List<Project> userProjects = iProjectRepository.readAllByUserId(currentUser.getUserId());
        // Konverterer User + projects til DTO
        UserWithProjectsDTO userWithProjectsDTO = new UserWithProjectsDTO(currentUser, userProjects);
        AssignCheck.ensureUserAssignedToProject(userWithProjectsDTO, project.getProjectId());


        // Deadline-håndtering (hvis ikke sat, behold eksisterende)
        Project existingProject = readById(project.getProjectId());
        project.setDeadline(
                DeadlineCheck.checkForDeadlineInput(project.getDeadline(), existingProject.getDeadline())
        );


        // Statusvalidering: Project må kun sættes til DONE, hvis alle SubProjects er DONE
        if (project.getStatus() == Status.DONE) {
            List<SubProject> subProjectList = iSubProjectRepository.readAllFromProjectId(project.getProjectId());
            ProjectWithSubProjectsDTO projectWithSubProjectsDTO = new ProjectWithSubProjectsDTO(project, subProjectList);

            if (!statusCheck.canMarkProjectAsDone(projectWithSubProjectsDTO)) {
                throw new IllegalStateException("Projektet kan ikke markeres som færdigt, før alle subprojekter er færdige.");
            }
        }

        return iProjectRepository.update(project);
    }

    //------------------------------------ Delete() ------------------------------------

    public void deleteById(int id, UserViewDTO currentUser) {

        // Kun admin og projektleder må slette et projekt
        RoleCheck.ensureAdminOrProjectManager(currentUser.getRole());
        iProjectRepository.deleteById(id);
    }

    //------------------------------------ DTO'er ------------------------------------


    // --- Find projekter for én bruger ---

    public List<Project> readByUserId(int userId) {

        return iProjectRepository.readAllByUserId(userId);
    }

    //------------------------------------ DTO-Mappings -------------------------------


    // --- Find brugere for ét projekt ---
    public ProjectWithUsersDTO readALlUsersByProjectId(int projectId) {

        // Henter ét projekt
        Project project = iProjectRepository.readById(projectId);

        // Henter tilknyttede brugere for dét projekt
        List<User> userList = iUserRepository.readAllByProjectId(projectId);

        // Hver User konverteres til UserViewDTO
        List<UserViewDTO> userViewDTOList = new ArrayList<>();
        for (User user : userList) {
            UserViewDTO userViewDTO = new UserViewDTO(
                    user.getUserId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getRole()
            );
            userViewDTOList.add(userViewDTO);
        }

        // Returnerer projektet + brugere
        return new ProjectWithUsersDTO(project, userViewDTOList);
    }

    // --- Find subprojekter for ét projekt ---

    public ProjectWithSubProjectsDTO readAllFromProjectId(int projectId) {
        Project project = iProjectRepository.readById(projectId);
        List<SubProject> subProjects = iSubProjectRepository.readAllFromProjectId(projectId);

        return new ProjectWithSubProjectsDTO(project, subProjects);
    }



    //----------------------- Assign User til Project GET-mapping -------------------------


    public List<UserViewDTO> readAllUnAssignedUsers(int projectId) {

        // Læser alle brugere
        List<User> allUserList = iUserRepository.readAll();


        // Læser projektets allerede tilknyttede brugere
        List<User> assignedUserList = iUserRepository.readAllByProjectId(projectId);

        // Samler ID'er på de allerede tildelte brugere
        Set<Integer> assignedUserIds = new HashSet<>();
        for (User user : assignedUserList) {
            assignedUserIds.add(user.getUserId());
        }

        // Tilføjer kun de brugere, der IKKE allerede er tildelt projektet
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

    //------------------------- Assign User til Project POST-mapping ------------------------


    public void assignUserToProject(UserViewDTO currentUser, List<Integer> userIds, int projectId) {

        // Kun admin og projektleder må assign bruger til projekt
        RoleCheck.ensureAdminOrProjectManager(currentUser.getRole());

        // Henter de brugere, der allerede er tilknyttet projektet
        List<User> existingUsers = iUserRepository.readAllByProjectId(projectId);

        // Opretter et tomt Set af brugerID'er
        Set<Integer> existingUserIds = new HashSet<>();

        // BrugerID'er gemmes i Settet (undgår duplikater)
        for (User user : existingUsers) {
            existingUserIds.add(user.getUserId());
        }

        // Tjekker om brugerID'et allerede ligger i databasen
        for (Integer userId : userIds) {
            if (!existingUserIds.contains(userId)) {
                iProjectRepository.assignUserToProject(userId, projectId);
            }
        }
    }

}
