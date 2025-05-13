package com.example.estimationtool.service;


import com.example.estimationtool.model.SubTask;
import com.example.estimationtool.model.User;
import com.example.estimationtool.model.timeEntry.TimeEntry;
import com.example.estimationtool.repository.interfaces.ISubTaskRepository;
import com.example.estimationtool.repository.interfaces.ITaskRepository;
import com.example.estimationtool.model.Task;

import java.util.ArrayList;
import java.util.List;

import com.example.estimationtool.repository.interfaces.ITimeEntryRepository;
import com.example.estimationtool.repository.interfaces.IUserRepository;
import com.example.estimationtool.toolbox.dto.*;
import com.example.estimationtool.toolbox.roleCheck.RoleCheck;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final ITaskRepository iTaskRepository;
    private final IUserRepository iUserRepository;
    private final ISubTaskRepository iSubTaskRepository;
    private final ITimeEntryRepository iTimeEntryRepository;

    public TaskService(ITaskRepository iTaskRepository, IUserRepository iUserRepository, ISubTaskRepository iSubTaskRepository, ITimeEntryRepository iTimeEntryRepository) {
        this.iTaskRepository = iTaskRepository;
        this.iUserRepository = iUserRepository;
        this.iSubTaskRepository = iSubTaskRepository;
        this.iTimeEntryRepository = iTimeEntryRepository;
    }

    //------------------------------------ Create() ------------------------------------

    public Task createTask(Task task) {
        return iTaskRepository.create(task);

    }

    //------------------------------------ Read() --------------------------------------

    public List<Task> readAll() {
        return iTaskRepository.readAll();
    }

    public Task readById(int id) {
        return iTaskRepository.readById(id);
    }

    //------------------------------------ Update() ------------------------------------

    public Task updateTask(Task task) {
        return iTaskRepository.update(task);
    }
    //------------------------------------ Delete() ------------------------------------

    public void deleteById(int id) {
        iTaskRepository.deleteById(id);
    }

    //------------------------------------ DTO'er -------------------------------------

    // --- Henter tasks ud fra brugerID for UserService ---

    public List<Task> readAllTasksByUserId(int userId) {

        return iTaskRepository.readAllByUserId(userId);
    }

    // --- Henter brugere ud fra taskID ---

    public TaskWithUsersDTO readAllUsersByTaskId(int taskId) {

        // Læser task ud fra taskID
        Task task = iTaskRepository.readById(taskId);

        // Læser tilknyttede brugere
        List<User> users = iUserRepository.readAllByTaskId(taskId);

        // Konverter til UserViewDTO
        List<UserViewDTO> userViewDTOList = new ArrayList<>();
        for (User user : users) {
            UserViewDTO userViewDTO = new UserViewDTO(
                    user.getUserId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getRole()
            );
            userViewDTOList.add(userViewDTO); // Tilføjer hver UserDTO til listen
        }
        // Returnerer task + liste af UserViewDTO
        return new TaskWithUsersDTO(task, userViewDTOList);

    }

    // --- Henter subtasks ud fra taskID ---

    public TaskWithSubTasksDTO readAllSubTasksByTaskId(int taskId) {
        // Hent opgaven
        Task task = iTaskRepository.readById(taskId);

        // Hent tilknyttede subtasks
        List<SubTask> subTaskList = iSubTaskRepository.readAllByTaskId(taskId);

        // Returnér samlet DTO
        return new TaskWithSubTasksDTO(task, subTaskList);
    }

    // --- Henter timeentries ud fra task-ID -----

    public TaskWithTimeEntriesDTO readAllTimeEntriesByTaskId(int taskId) {
        Task task = iTaskRepository.readById(taskId);
        List<TimeEntry> entries = iTimeEntryRepository.readAllByTaskId(taskId);
        return new TaskWithTimeEntriesDTO(task, entries);
    }



    //---------------------------------- Assign User --------------------------------

    // ----------------- Task tildeles en bruger efter oprettelse -------------------

    public void assignUserToTask(UserViewDTO currentUser, int userId, int taskId) {
        RoleCheck.ensureAdminOrProjectManager(currentUser.getRole());
        iTaskRepository.assignUserToTask(userId, taskId);
    }

}
