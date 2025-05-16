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

    public TimeEntryService(ITimeEntryRepository iTimeEntryRepository, ISubTaskRepository iSubTaskRepository, ITaskRepository iTaskRepository, ISubProjectRepository iSubProjectRepository, IProjectRepository iProjectRepository) {
        this.iTimeEntryRepository = iTimeEntryRepository;
        this.iSubTaskRepository = iSubTaskRepository;
        this.iTaskRepository = iTaskRepository;
        this.iSubProjectRepository = iSubProjectRepository;
        this.iProjectRepository = iProjectRepository;
    }

    //------------------------------------ Create() ------------------------------------

    public TimeEntry createTimeEntry(TimeEntry timeEntry) {
        updateTimeSpentForAll(timeEntry);
        return iTimeEntryRepository.create(timeEntry);
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
        updateTimeSpentForAll(timeEntry);
        return iTimeEntryRepository.update(timeEntry);
    }
    //------------------------------------ Delete() ------------------------------------

    public void deleteById(int id) {

        iTimeEntryRepository.deleteById(id);
    }

    //------------- Opdatering af tidsforbrug hos alle projektkomponenter --------------

    public void updateTimeSpentForAll(TimeEntry timeEntry) {
        updateTimeSpentForTask(timeEntry);
        updateTimeSpentForSubProject(timeEntry);
        updateTimeSpentForProject(timeEntry);
    }

    //----------------- Opdatering af tidsforbrug hos tasks og subtasks -----------------

    public void updateTimeSpentForTask(TimeEntry timeEntry) {
        // TODO Dette tjek skal jeg ændre på et tidspunkt, for vores H2-testdatabase kører ikke med de samme ID'er som vores normale database x.x
        // Først tjekkes om taskens id findes
        if (timeEntry.getTaskId() > 0) {
            // Der oprettes en liste af subtasks under denne task
            List<SubTask> subTasks = iSubTaskRepository.readAllByTaskId(timeEntry.getTaskId());
            // Der tjekkes om listen er tom. Hvis den er det, så har tasken ingen subtasks og den opdaterede tid skal
            // ske direkte ud fra tasken og dens time entries
            if (subTasks.isEmpty()) {
                updateTimeSpentForTaskWithTimeEntries(timeEntry);
            } else {
                updateTimeSpentForTaskWithSubTasks(timeEntry);
            }
        }
    }

    // Extracted fra updateTimeSpentForTask for bedre læsbarhed
    // Bruger TimeEntries der ligger direkte hos en Task, som ikke har nogle underliggende SubTasks
    private void updateTimeSpentForTaskWithTimeEntries(TimeEntry timeEntry) {
        int timeSpentForTask = 0;
        // Vi gemmer taskens id ud fra TimeEntry-parametren
        int taskId = timeEntry.getTaskId();
        // Vi laver en lokal Task-variabel ud fra taskId
        Task task = iTaskRepository.readById(timeEntry.getTaskId());
        // Vi gemmer en liste af TimeEntries der er logget på denne task
        List<TimeEntry> timeEntries = iTimeEntryRepository.readAllByTaskId(taskId);
        // Vi sætter timeSpent-attributten hos task-variablen til den udregnede værdi. Dette sker vha. TimeCalculatoren
        timeSpentForTask = TimeCalculator.calculateTimeSpent(timeEntries);
        task.setTimeSpent(timeSpentForTask);
        // Vi opdaterer den givne Task ud fra vores lokale task-variabel som er identisk med den i databasen, udover at have en opdateret timeSpent-værdi
        iTaskRepository.update(task);
    }

    // Extracted fra updateTimeSpentForTask for bedre læsbarhed
    // Bruger SubTasks under en Task til
    private void updateTimeSpentForTaskWithSubTasks(TimeEntry timeEntry) {
        int timeSpentForTask = 0;
        // Hvis ikke listen af subtasks under den givne task er tom, dvs. der er subtasks under tasken, skal vi et led længere ned
        // Vi laver en lokal Task-variabel ud fra taskId, som bliver taget fra timeEntry-parametren i metoden
        Task task = iTaskRepository.readById(timeEntry.getTaskId());
        // For hver subtask der ligger under denne task skal vi udføre en række operationer
        for (SubTask subTask : iSubTaskRepository.readAllByTaskId(timeEntry.getTaskId())) {
            int timeSpentForSubTask;
            // Vi gemmer taskens id ud fra TimeEntry-parametren
            int subTaskId = timeEntry.getSubTaskId();
            // // Vi gemmer en liste af TimeEntries der er logget på denne subtask
            List<TimeEntry> timeEntries = iTimeEntryRepository.readAllBySubTaskId(subTaskId);
            // Vi sætter timeSpent-attributten hos subtask-variablen defineret i vores for-each loop
            // til den udregnede værdi. Dette sker vha. TimeCalculatoren
            timeSpentForSubTask = TimeCalculator.calculateTimeSpent(timeEntries);
            subTask.setTimeSpent(timeSpentForSubTask);
            timeSpentForTask += timeSpentForSubTask;
            // Vi opdaterer de to objekters tilsvarende tabelrækker i databasen med repository-update-metoderne
            iSubTaskRepository.update(subTask);
        }
        // Vi sætter timeSpent-attributten hos tasken der "ejer" subtasken til at være den tidligere værdi
        //  plus den værdi der ligger hos subtasken
        task.setTimeSpent(timeSpentForTask); // TODO Skal fikses på et tidspunkt så den også tager højde for, at tid sættes ned
        iTaskRepository.update(task);
    }

    //-------------------- Opdatering af tidsforbrug hos subprojekter --------------------

    private void updateTimeSpentForSubProject(TimeEntry timeEntry) {
        // Henter taskId fra timeEntry
        int taskId = timeEntry.getTaskId();
        // Henter Task fra taskId så vi kan hente subProjectId fra task
        Task taskForGettingSubProjectId = iTaskRepository.readById(taskId);
        // Henter subProjectId fra task
        int subProjectId = taskForGettingSubProjectId.getSubProjectId();
        // Henter SubProjekt fra subProjectId
        SubProject subProject = iSubProjectRepository.readById(subProjectId);
        // Henter listen af Tasks under et SubProject med subProjectId
        List<Task> tasksUnderSubProject = iTaskRepository.readAllBySubProjectId(subProjectId);
        // Opretter lokal variabel til at opbevare det summerede tidsforbrug på subprojektet
        int timeSpentOnSubProject = 0;
        // timeSpent bliver summeret i et for-each loop ud fra hver Task i listen af Tasks under et SubProject
        for (Task task : tasksUnderSubProject) {
            timeSpentOnSubProject += task.getTimeSpent();
        }
        // Den opsummerede timeSpent-værdi sættes i project og værdien opdateres i databasetabellen
        subProject.setTimeSpent(timeSpentOnSubProject);
        iSubProjectRepository.update(subProject);
    }

    //-------------------- Opdatering af tidsforbrug hos projekter -----------------------

    private void updateTimeSpentForProject(TimeEntry timeEntry) {
        // Henter taskId fra timeEntry
        int taskId = timeEntry.getTaskId();
        // Henter Task fra taskId så vi kan hente subProjectId fra task
        Task taskForGettingSubProjectId = iTaskRepository.readById(taskId);
        // Henter subProjectId fra task
        int subProjectId = taskForGettingSubProjectId.getSubProjectId();
        // Henter SubProjekt fra subProjectId så vi kan hente projectId fra subProject
        SubProject subProjectForGettingProjectId = iSubProjectRepository.readById(subProjectId);
        // Henter projectId fra subProject
        int projectId = subProjectForGettingProjectId.getProjectId();
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
    }

    //-------------------- Opdatering af tidsforbrug ved sletning -----------------------
//
//    private void updateSubTaskOnDelete(SubTask subTask) {
//        int subTaskId = subTask.getSubTaskId();
//        List<TimeEntry> timeEntries = iTimeEntryRepository.readAllBySubTaskId(subTask.getSubTaskId());
//        for (TimeEntry timeEntry : timeEntries) {
//            timeEntry.setHoursSpent(0);
//        }
//    }
}
