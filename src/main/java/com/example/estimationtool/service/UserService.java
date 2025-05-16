package com.example.estimationtool.service;

import com.example.estimationtool.model.*;

import com.example.estimationtool.toolbox.dto.*;

import com.example.estimationtool.repository.interfaces.IUserRepository;
import com.example.estimationtool.toolbox.check.RoleCheck;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

// DATABASEFEJL BOBLER OP til @ControllerAdvice pga. JdbcTemplate fordi det er en unchecked exception
//VALIDERING, eks. login-fejl, KASTER VI EXCEPTION (throws) for at signalere en fejl

@Service
public class UserService {

    private final IUserRepository iUserRepository;
    private final PasswordEncoder passwordEncoder;

    private final ProjectService projectService;
    private final SubProjectService subProjectService;
    private final SubTaskService subTaskService;
    private final TaskService taskService;



    public UserService(IUserRepository iUserRepository, PasswordEncoder passwordEncoder,
                       ProjectService projectService, SubProjectService subProjectService,
                       SubTaskService subTaskService, TaskService taskService) {
        this.iUserRepository = iUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.projectService = projectService;
        this.subProjectService = subProjectService;
        this.subTaskService = subTaskService;
        this.taskService = taskService;
    }

    //------------------------------------ Create() ------------------------------------

    public User createUser(UserViewDTO currentUser, UserRegistrationDTO userDTO) {

        RoleCheck.ensureAdmin(currentUser.getRole()); // Adgangskontrol

        // Mapper DTO til User-objekt med hashet password
        User newUser = new User(
                0,
                userDTO.getFirstName(),
                userDTO.getLastName(),
                userDTO.getEmail(),
                passwordEncoder.encode(userDTO.getPassword()), // Hashing
                userDTO.getRole()
        );

        return iUserRepository.create(newUser);
    }


    //------------------------------------ Read() --------------------------------------

    public List<UserViewDTO> readAll() {

        List<User> userList = iUserRepository.readAll();
        List<UserViewDTO> userViewDTOList = new ArrayList<>();

        // Konverterer userList til userViewDTOList
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
        return userViewDTOList;

    }

    public UserViewDTO readById(int id) {
        User user = iUserRepository.readById(id);
        if (user == null) {
            throw new NoSuchElementException("Bruger med ID " + id + " eksisterer ikke.");
        }

        // Konverterer User til UserViewDTO
        return new UserViewDTO(
                user.getUserId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole()
        );

    }

    //------------------------------------ Update() ------------------------------------

    public User updateUser(UserUpdateDTO userUpdateDTO, UserViewDTO currentUser) {

        // Henter eksisterende bruger
        User existingUser = iUserRepository.readById(userUpdateDTO.getUserId());

        // Tjekker om bruger findes
        if (existingUser == null) {
            throw new RuntimeException("Bruger med ID: " + userUpdateDTO.getUserId() + " eksisterer ikke.");
        }

        // Kun admin må redigere en bruger
        RoleCheck.ensureAdmin(currentUser.getRole());


        // Håndterer password
        String passwordHash = getPasswordHash(userUpdateDTO, existingUser);

        // Mapper UserUpdateDTO til User-objekt med opdateret bruger
        User updatedUser = new User(
                userUpdateDTO.getUserId(),
                userUpdateDTO.getFirstName(),
                userUpdateDTO.getLastName(),
                userUpdateDTO.getEmail(),
                passwordHash,
                userUpdateDTO.getRole() // Kun admin må ændre brugerens rolle
        );

        return iUserRepository.update(updatedUser);

    }



    //------------------------------------ Delete() ------------------------------------

    public void deleteById(int id, UserViewDTO currentUser) {

        // Kun admin må slette en bruger
        RoleCheck.ensureAdmin(currentUser.getRole());

        iUserRepository.deleteById(id);

    }


    //---------------------------------- Henter Login() -----------------------------------

    public UserViewDTO login(String email, String inputPassword) {

        User user = iUserRepository.readByEmail(email);

        // Matcher input-password med databasens hashet password
        if (passwordEncoder.matches(inputPassword, user.getPasswordHash())) {
            return new UserViewDTO(
                    user.getUserId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getRole()
            );
        }
        throw new BadCredentialsException("Adgangskoden er forkert.");
    }

    //----------------- Henter UserUpdateDTO for GET-mapping: showEditUser() --------------

    public UserUpdateDTO getUserUpdateDTOById(int id) {
        UserViewDTO userViewDTO = readById(id);

        return new UserUpdateDTO(
                userViewDTO.getUserId(),
                userViewDTO.getFirstName(),
                userViewDTO.getLastName(),
                userViewDTO.getEmail(),
                "", // Tomt passwordfelt, skal indtastes af brugeren
                userViewDTO.getRole()
        );
    }

    //--------------------------------- Henter PasswordHash --------------------------------


    private String getPasswordHash(UserUpdateDTO userUpdateDTO, User existingUser) {
        String passwordHash;

        if (userUpdateDTO.getPassword() == null || userUpdateDTO.getPassword().isBlank()) {
            // Hvis intet password er angivet, behold eksisterende hash
            passwordHash = existingUser.getPasswordHash();

        } else if (passwordEncoder.matches(userUpdateDTO.getPassword(), existingUser.getPasswordHash())) {
            // Hvis brugeren indtaster det samme password = behold det
            passwordHash = existingUser.getPasswordHash();

        } else {
            // Hash opdaterede password
            passwordHash = passwordEncoder.encode(userUpdateDTO.getPassword());
        }
        return passwordHash;
    }


    //------------------------------------ DTO-Mappings -----------------------------------


    // --- Henter projekter ud fra brugerID ---

    public UserWithProjectsDTO readAllProjectsByUserId(int userId) {

        // Læser én bruger
        User user = iUserRepository.readById(userId);

        // ExceptionHandling
//        if (user == null) {
//            throw new NoSuchElementException("Bruger med ID " + userId + " findes ikke.");
//        }

        // Konverterer User til UserViewDTO
        UserViewDTO userViewDTO = new UserViewDTO(
                user.getUserId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole()
        );

        // Læser projekter ud fra brugerID
        List<Project> projectList = projectService.readByUserId(userId);

        // Returnerer bruger + projekter i en DTO
        return new UserWithProjectsDTO(userViewDTO, projectList);
    }


    // --- Henter subprojekter ud fra brugerID ---

    public UserWithSubProjectsDTO readAllSubProjectsByUserId(int userId) {

        // Læser én bruger
        User user = iUserRepository.readById(userId);

        // ExceptionHandling
//        if (user == null) {
//            throw new NoSuchElementException("Bruger med ID " + userId + " findes ikke.");
//        }

        // Konverterer User til UserViewDTO
        UserViewDTO userViewDTO = new UserViewDTO(
                user.getUserId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole()
        );

        List<SubProject> subProjectList = subProjectService.readAllSubProjectsByUserId(userId);

        return new UserWithSubProjectsDTO(userViewDTO, subProjectList);

    }

    // --- Henter tasks ud fra brugerID ---

    public UserWithTasksDTO readAllTasksByUserId(int userId) {

        // Henter bruger ud fra brugerID
        User user = iUserRepository.readById(userId);

        // Konverterer User til UserViewDTO
        UserViewDTO userViewDTO = new UserViewDTO(
                user.getUserId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole()
        );

        List<Task> tasks = taskService.readAllTasksByUserId(userId);

        return new UserWithTasksDTO(userViewDTO, tasks);
    }

    // --- Henter subtasks ud fra brugerID ---

    public UserWithSubTasksDTO readAllSubTasksByUserId(int userId) {

        // Læser én bruger
        User user = iUserRepository.readById(userId);

        // Konverter user til UserViewDTO
        UserViewDTO userViewDTO = new UserViewDTO(
                user.getUserId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole()
        );

        // Læser subtasks ud fra brugerID
        List<SubTask> subTaskList = subTaskService.readAllSubTasksByUserId(userId);

        // Returnerer userViewDTO + subtasks i én samlet DTO
        return new UserWithSubTasksDTO(userViewDTO, subTaskList);
    }


}
