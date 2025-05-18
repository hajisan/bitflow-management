package com.example.estimationtool.service;

import com.example.estimationtool.model.Project;
import com.example.estimationtool.model.SubProject;
import com.example.estimationtool.model.SubTask;
import com.example.estimationtool.model.Task;
import com.example.estimationtool.model.timeEntry.TimeEntry;

import com.example.estimationtool.repository.interfaces.*;

import com.example.estimationtool.toolbox.timeCalc.TimeCalculator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimeEntryService {

    private final ITimeEntryRepository iTimeEntryRepository;
    private final ISubTaskRepository iSubTaskRepository;
    private final ITaskRepository iTaskRepository;
    private final ISubProjectRepository iSubProjectRepository;
    private final IProjectRepository iProjectRepository;

    public TimeEntryService(ITimeEntryRepository iTimeEntryRepository,
                            ISubTaskRepository iSubTaskRepository,
                            ITaskRepository iTaskRepository,
                            ISubProjectRepository iSubProjectRepository,
                            IProjectRepository iProjectRepository) {
        this.iTimeEntryRepository = iTimeEntryRepository;
        this.iSubTaskRepository = iSubTaskRepository;
        this.iTaskRepository = iTaskRepository;
        this.iSubProjectRepository = iSubProjectRepository;
        this.iProjectRepository = iProjectRepository;
    }

    //------------------------------------ Create() ------------------------------------

    public TimeEntry createTimeEntry(TimeEntry timeEntry) {
        TimeEntry createdTimeEntry = iTimeEntryRepository.create(timeEntry);
        updateTimeSpentForComponents(timeEntry);
        // Sørger for at kalde repository-CRUD-metoden før opdateringen af timeSpent
        return createdTimeEntry;
    }

    //------------------------------------ Read() --------------------------------------

    public List<TimeEntry> readAll() {
        return iTimeEntryRepository.readAll();
    }

    public TimeEntry readById(int id) {
        return iTimeEntryRepository.readById(id);
    }
    //------------------------------------ Update() ------------------------------------

    public TimeEntry updateTimeEntry(TimeEntry timeEntry) {
        TimeEntry updatedTimeEntry = iTimeEntryRepository.update(timeEntry);
        updateTimeSpentForComponents(timeEntry);
        // Sørger for at kalde repository-CRUD-metoden før opdateringen af timeSpent
        return updatedTimeEntry;
    }
    //------------------------------------ Delete() ------------------------------------

    public void deleteById(int id) {

        iTimeEntryRepository.deleteById(id);

    }

    //------------- Opdatering af tidsforbrug hos alle projektkomponenter --------------

    public void updateTimeSpentForComponents(TimeEntry timeEntry) {
        // Hvis der er registreret en subtask på den givne TimeEntry, skal vi regne den med:
        if (timeEntry.getSubTaskId() > 0) {
            int subTaskId = timeEntry.getSubTaskId(); // Henter subTaskId fra timeEntry
            updateTimeSpentForSubTaskWithId(subTaskId); // Opdaterer SubTask med subTaskId
            int taskId = iSubTaskRepository.readById(subTaskId).getTaskId(); // Henter taskId fra SubTask, som er hentet fra sit eget subTaskId
            updateTimeSpentForTaskWithId(taskId); // Opdaterer Task med taskId
            int subProjectId = iTaskRepository.readById(taskId).getSubProjectId(); // Henter subProjectId fra Task, som er hentet fra sit eget taskId
            updateTimeSpentForSubProjectWithId(subProjectId); // Opdaterer SubProject med subProjectId
            int projectId = iSubProjectRepository.readById(subProjectId).getProjectId(); // Henter projectId fra subProject, som er hentet fra sit eget subProjectId
            updateTimeSpentForProjectWithId(projectId); // Opdaterer Project med projectId
        }
        // Hvis der ikke er registreret en subtask, springer vi det trin over og kører således:
        else if (timeEntry.getTaskId() > 0) {
            int taskId = timeEntry.getTaskId(); // Henter taskId fra timeEntry
            updateTimeSpentForTaskWithId(taskId); // Opdaterer Task med taskId
            int subProjectId = iTaskRepository.readById(taskId).getSubProjectId();// Henter subProjectId fra Task, som er hentet fra sit eget taskId
            updateTimeSpentForSubProjectWithId(subProjectId); // Opdaterer SubProject med subProjectId
            int projectId = iSubProjectRepository.readById(subProjectId).getProjectId(); // Henter projectId fra subProject, som er hentet fra sit eget subProjectId
            updateTimeSpentForProjectWithId(projectId); // Opdaterer Project med projectId
        }
    }

    //---------------- Opdatering af tidsforbrug hos projektkomponenter -----------------

    //-------------------------------- Opdatering med ID --------------------------------

    //----------------- Opdatering af tidsforbrug hos tasks og subtasks -----------------

    //                                  --- SUBTASK M. ID ---
    // Metoden tager et subtask-id og opdaterer timeSpent for den givne subtask
    public int updateTimeSpentForSubTaskWithId(int subTaskId) {
        SubTask subTask = iSubTaskRepository.readById(subTaskId);
        List<TimeEntry> timeEntries = iTimeEntryRepository.readAllBySubTaskId(subTaskId);
        int timeSpentOnSubTask = TimeCalculator.calculateTimeSpent(timeEntries);
        subTask.setTimeSpent(timeSpentOnSubTask);
        iSubTaskRepository.update(subTask);
        System.out.println("Tid brugt på SubTask med id: " + subTaskId + " er " + timeSpentOnSubTask);
        return timeSpentOnSubTask;
    }

    //                                    --- TASK M. ID ---
    // Metoden tager et task-id og opdaterer timeSpent for den givne task
    public int updateTimeSpentForTaskWithId(int taskId) {

        Task task = iTaskRepository.readById(taskId);

        int timeSpentOnTask = 0;

        List<SubTask> subTasks = iSubTaskRepository.readAllByTaskId(task.getTaskId());

        if (subTasks.isEmpty()) {
            List<TimeEntry> timeEntries = iTimeEntryRepository.readAllByTaskId(taskId);
            timeSpentOnTask = TimeCalculator.calculateTimeSpent(timeEntries);
            System.out.println("Tid brugt på Task med id: " + taskId + " er " + timeSpentOnTask);
        } else {
            int i = 1;
            for (SubTask subTask : subTasks) {
                System.out.println("Tid brugt på SubTask med id: " + subTask.getSubTaskId() + " er " + subTask.getTimeSpent());
                timeSpentOnTask += subTask.getTimeSpent();
                System.out.println("Tid brugt på Task med id: " + taskId + " er nu" + timeSpentOnTask + " i loopet på subtask nummer " + i + "ud af " + subTasks.size());
                i++;
            }
        }
        System.out.println("Tid brugt på Task med id: " + taskId + " er " + timeSpentOnTask + " efter loopet");
        task.setTimeSpent(timeSpentOnTask);
        iTaskRepository.update(task);

        return timeSpentOnTask;
    }

    //-------------------- Opdatering af tidsforbrug hos subprojekter --------------------

    //                               --- SUBPROJEKT M. ID ---
    // Metoden tager et subprojekt-id og opdaterer timeSpent hos det givne subprojekt
    public int updateTimeSpentForSubProjectWithId(int subProjectId) {
        // Opretter en lokal SubProject-instans ud fra parametren til at opdatere timeSpent hos
        SubProject subProject = iSubProjectRepository.readById(subProjectId);
        // Henter listen af Tasks under et SubProject med subProjectId
        List<Task> tasks = iTaskRepository.readAllBySubProjectId(subProjectId);
        // Opretter lokal variabel til at opbevare det summerede tidsforbrug på subprojektet
        int timeSpentOnSubProject = 0;

        int i = 1;
        for (Task task : tasks) {

            System.out.println("Tid brugt på Task med id: " + task.getTaskId() + " er " + task.getTimeSpent());
            timeSpentOnSubProject += task.getTimeSpent();
            System.out.println("Tid brugt på SubProject med id: " + subProjectId + " er nu" + timeSpentOnSubProject + " i loopet på task nummer " + i + "ud af " + tasks.size());
            i++;
        }

        subProject.setTimeSpent(timeSpentOnSubProject);
        iSubProjectRepository.update(subProject);

        return timeSpentOnSubProject;

    }

    //-------------------- Opdatering af tidsforbrug hos projekter -----------------------

    //                               --- PROJEKT M. ID ---
    // Metoden tager et projekt-id og opdaterer timeSpent hos det givne projekt
    public int updateTimeSpentForProjectWithId(int projectId) {
        // Henter Project fra projectId
        Project project = iProjectRepository.readById(projectId);
        // Henter listen af SubProjects under et Project med projectId
        List<SubProject> subProjectsUnderProject = iSubProjectRepository.readAllFromProjectId(projectId);
        // Opretter lokal variabel til at opbevare det summerede tidsforbrug på projektet
        int timeSpentOnProject = 0;
        // timeSpent bliver summeret i et for-each loop ud fra hvert SubProject i listen af SubProjects under et Project
        for (SubProject subProject : subProjectsUnderProject) {
            timeSpentOnProject += subProject.getTimeSpent();
        }
        // Den opsummerede timeSpent-værdi sættes i project og værdien opdateres i databasetabellen
        project.setTimeSpent(timeSpentOnProject);
        iProjectRepository.update(project);
        return timeSpentOnProject;
    }
}
