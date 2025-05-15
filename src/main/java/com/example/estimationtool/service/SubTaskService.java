package com.example.estimationtool.service;


import com.example.estimationtool.model.SubTask;
import com.example.estimationtool.model.enums.Status;
import com.example.estimationtool.model.timeEntry.TimeEntry;
import com.example.estimationtool.repository.interfaces.ISubTaskRepository;
import com.example.estimationtool.repository.interfaces.ITimeEntryRepository;
import com.example.estimationtool.toolbox.check.StatusCheck;
import com.example.estimationtool.toolbox.dto.SubTaskWithTimeEntriesDTO;
import com.example.estimationtool.toolbox.dto.UserViewDTO;
import com.example.estimationtool.toolbox.check.RoleCheck;
import com.example.estimationtool.toolbox.timeCalc.TimeCalculator;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SubTaskService {

    private final ISubTaskRepository iSubTaskRepository;
    private final ITimeEntryRepository iTimeEntryRepository;
    private final StatusCheck statusCheck;

    public SubTaskService(ISubTaskRepository iSubTaskRepository, ITimeEntryRepository iTimeEntryRepository, StatusCheck statusCheck) {
        this.iSubTaskRepository = iSubTaskRepository;
        this.iTimeEntryRepository = iTimeEntryRepository;
        this.statusCheck = statusCheck;
    }

    //------------------------------------ Create() ------------------------------------

    public SubTask createSubTask(SubTask subTask) {
        return iSubTaskRepository.create(subTask);
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
                throw new IllegalStateException("Subtasken kan ikke markers som færdig");
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
        subTask.setTimeSpent(TimeCalculator.calculateTimeSpent(entries));

        return new SubTaskWithTimeEntriesDTO(subTask, entries);
    }

    //---------------------------------- Assign User ---------------------------------

    // ----------------- SubTask tildeles en bruger efter oprettelse -----------------

    public void assignUserToSubTask(UserViewDTO currentUser, int userId, int subTaskId) {

        RoleCheck.ensureAdminOrProjectManager(currentUser.getRole());

        iSubTaskRepository.assignUserToSubTask(userId, subTaskId);
    }



}
