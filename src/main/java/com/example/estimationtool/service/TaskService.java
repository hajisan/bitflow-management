package com.example.estimationtool.service;


import com.example.estimationtool.model.SubTask;
import com.example.estimationtool.model.User;
import com.example.estimationtool.model.enums.Status;
import com.example.estimationtool.model.timeEntry.TimeEntry;
import com.example.estimationtool.repository.interfaces.ISubTaskRepository;
import com.example.estimationtool.repository.interfaces.ITaskRepository;
import com.example.estimationtool.model.Task;

import java.util.ArrayList;
import java.util.List;

import com.example.estimationtool.repository.interfaces.ITimeEntryRepository;
import com.example.estimationtool.repository.interfaces.IUserRepository;
import com.example.estimationtool.toolbox.check.StatusCheck;
import com.example.estimationtool.toolbox.dto.*;
import com.example.estimationtool.toolbox.timeCalc.TimeCalculator;
import com.example.estimationtool.toolbox.check.RoleCheck;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final ITaskRepository iTaskRepository;
    private final IUserRepository iUserRepository;
    private final ISubTaskRepository iSubTaskRepository;
    private final ITimeEntryRepository iTimeEntryRepository;
    private final StatusCheck statusCheck;

    public TaskService(ITaskRepository iTaskRepository,
                       IUserRepository iUserRepository, ISubTaskRepository iSubTaskRepository,
                       ITimeEntryRepository iTimeEntryRepository, StatusCheck statusCheck) {
        this.iTaskRepository = iTaskRepository;
        this.iUserRepository = iUserRepository;
        this.iSubTaskRepository = iSubTaskRepository;
        this.iTimeEntryRepository = iTimeEntryRepository;
        this.statusCheck = statusCheck;
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

        if (task.getStatus() == Status.DONE) {
            List<SubTask> subTaskList = iSubTaskRepository.readAllByTaskId(task.getTaskId());
            // Konverterer Task + SubTasks til DTO
            TaskWithSubTasksDTO taskWithSubTasksDTO = new TaskWithSubTasksDTO(task, subTaskList);

            if (!statusCheck.canMarkTaskAsDone(taskWithSubTasksDTO)) {
                throw new IllegalStateException("Status for task kan ikke markeres som færdig, før alle SubTasks er færdige.");
            }
        }

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
        // Bruger TimeCalculatorens statiske metode til at sætte timeSpent ud fra de loggede TimeEntries
        task.setTimeSpent(TimeCalculator.calculateTimeSpent(entries));
        return new TaskWithTimeEntriesDTO(task, entries);
    }


    //---------------------------------- Assign User --------------------------------

    // ----------------- Task tildeles en bruger efter oprettelse -------------------

    public void assignUserToTask(UserViewDTO currentUser, int userId, int taskId) {
        RoleCheck.ensureAdminOrProjectManager(currentUser.getRole());
        iTaskRepository.assignUserToTask(userId, taskId);
    }

}
