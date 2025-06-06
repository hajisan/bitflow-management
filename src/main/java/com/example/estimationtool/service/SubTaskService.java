package com.example.estimationtool.service;


import com.example.estimationtool.model.SubTask;
import com.example.estimationtool.model.User;
import com.example.estimationtool.model.enums.Role;
import com.example.estimationtool.model.enums.Status;
import com.example.estimationtool.model.TimeEntry;
import com.example.estimationtool.repository.interfaces.ISubTaskRepository;
import com.example.estimationtool.repository.interfaces.ITimeEntryRepository;
import com.example.estimationtool.repository.interfaces.IUserRepository;
import com.example.estimationtool.toolbox.check.StatusCheck;
import com.example.estimationtool.toolbox.controllerAdvice.UserFriendlyException;
import com.example.estimationtool.toolbox.dto.SubTaskWithTimeEntriesDTO;
import com.example.estimationtool.toolbox.dto.UserViewDTO;
import com.example.estimationtool.toolbox.timeCalc.TimeCalculator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubTaskService {

    private final ISubTaskRepository iSubTaskRepository;
    private final ITimeEntryRepository iTimeEntryRepository;
    private final IUserRepository iUserRepository;
    private final StatusCheck statusCheck;
    private final TimeCalculator timeCalculator;

    public SubTaskService(ISubTaskRepository iSubTaskRepository, ITimeEntryRepository iTimeEntryRepository, IUserRepository iUserRepository, StatusCheck statusCheck, TimeCalculator timeCalculator) {
        this.iSubTaskRepository = iSubTaskRepository;
        this.iTimeEntryRepository = iTimeEntryRepository;
        this.iUserRepository = iUserRepository;
        this.statusCheck = statusCheck;
        this.timeCalculator = timeCalculator;
    }

    //------------------------------------ Create() ------------------------------------

    public SubTask createSubTask(UserViewDTO currentUser, SubTask subTask) {
        SubTask createdSubTask = iSubTaskRepository.create(subTask);
        iSubTaskRepository.assignUserToSubTask(currentUser.getUserId(), subTask.getSubTaskId());
        return createdSubTask;
    }

    //------------------------------------ Read() --------------------------------------

    public List<SubTask> readAll() {
        return iSubTaskRepository.readAll();
    }

    public SubTask readById(int id) {
        return iSubTaskRepository.readById(id);
    }

    //------------------------------------ Update() ------------------------------------

    public SubTask updateSubTask(SubTask subtask) {


        // Statusvalidering: formelt for konsistens i struktur. En SubTask har ingen underopgaver tilknyttet
        if (subtask.getStatus() == Status.DONE) {
            if (!statusCheck.canMarkSubTaskAsDone(subtask)) {
                throw new IllegalStateException("Subtasken kan ikke markeres som færdig.");
            }
        }
        return iSubTaskRepository.update(subtask);
    }

    //------------------------------------ Delete() ------------------------------------

    public void deleteById(int id) {
        iSubTaskRepository.deleteById(id);
    }

    //------------------------------------ Til DTO'er ----------------------------------


    // --- Henter subtasks ud fra brugerID for UserService ---
    public List<SubTask> readAllSubTasksByUserId(int userId) {

        return iSubTaskRepository.readAllByUserId(userId);
    }


    // --- Henter timeentries ud fra subtask-ID ---

    public SubTaskWithTimeEntriesDTO readAllTimeEntriesBySubTaskId(int subTaskId) {

        SubTask subTask = iSubTaskRepository.readById(subTaskId);
        List<TimeEntry> entries = iTimeEntryRepository.readAllBySubTaskId(subTaskId);
        // Bruger TimeCalculatorens statiske metode til at sætte timeSpent ud fra de loggede TimeEntries
        subTask.setTimeSpent(timeCalculator.calculateTimeSpent(entries));

        return new SubTaskWithTimeEntriesDTO(subTask, entries);
    }

    //---------------------------------- Assign User ---------------------------------

    // ----------------- SubTask tildeles en bruger efter oprettelse -----------------

    public void assignUserToSubTask(UserViewDTO currentUser, int userId, int subTaskId) {

        // Developer må kun tildele sig selv
        if (currentUser.getRole() == Role.DEVELOPER && currentUser.getUserId() != userId) {
            throw new UserFriendlyException("Udviklere må kun tildele sig selv til en subtask.", "/subtasks/" + subTaskId);
        }

        iSubTaskRepository.assignUserToSubTask(userId, subTaskId);
    }

    // ---------------- Read() hvem der er assignet til subtask ---------------------


    public UserViewDTO readAssignedUserBySubTaskId(int subTaskId) {

        // Find bruger på subTask
        User user = iUserRepository.readUserBySubTaskId(subTaskId);
//        if (user == null) {
//            throw new UserFriendlyException("Der var ingen bruger til denne subtask.", "/users/profile");
//        }
        return new UserViewDTO(
                user.getUserId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole()
        );
    }

    // Denne metode findes også i TaskService
    public List<UserViewDTO> readAllAvailableUsers() {

        // Læser alle brugere, da man pr. definition er unassigned
        List<User> allUserList = iUserRepository.readAll();

        // Tilføjer kun de brugere, der IKKE allerede er tildelt task
        List<UserViewDTO> usersDTO = new ArrayList<>();
        for (User user : allUserList) {

            usersDTO.add(new UserViewDTO(
                    user.getUserId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getRole()
            ));

        }

        return usersDTO;
    }

}
