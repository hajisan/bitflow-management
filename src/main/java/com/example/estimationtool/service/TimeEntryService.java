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
        if (timeEntry.getSubTaskId() < 0) {
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

//    public void updateTimeSpentForAll(TimeEntry timeEntry) {
//        updateTimeSpentForTask(timeEntry); // updateTimeSpentForSubTask kaldes i updateTimeSpentForTask
//        updateTimeSpentForSubProject(timeEntry);
//        updateTimeSpentForProject(timeEntry);
//    }

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
            for (SubTask subTask : subTasks) {
                int i = 1;
                System.out.println("Tid brugt på SubTask med id: " + subTask.getSubTaskId() + " er " + subTask.getTimeSpent());
                timeSpentOnTask += subTask.getTimeSpent();
                System.out.println("Tid brugt på Task med id: " + taskId + " er nu" + timeSpentOnTask + " i loopet på subtask nummer " + i + "ud af " + subTasks.size());
            }
        }
        System.out.println("Tid brugt på Task med id: " + taskId + " er " + timeSpentOnTask + " efter loopet");
        task.setTimeSpent(timeSpentOnTask);
        iTaskRepository.update(task);

        return timeSpentOnTask;
    }

//    // Hjælpemetode til at opdatere timeSpent for task med id. Køres hvis tasken indeholder subtasks
//    private int updateTimeSpentForTaskWithIdAndSubTasks(int taskId) {
//        List<SubTask> subTasks = iSubTaskRepository.readAllByTaskId(taskId);
//        int timeSpentOnTask = 0;
//        for (SubTask subTask : subTasks) {
//            timeSpentOnTask += updateTimeSpentForSubTaskWithId(subTask.getSubTaskId());
//        }
//        return timeSpentOnTask;
//    }

//    // Hjælpemetode til at opdatere timeSpent for task med id. Køres hvis tasken ikke indeholder subtasks, men time entries
//    private int updateTimeSpentForTaskWithIdAndTimeEntries(int taskId, Task task) {
//        List<TimeEntry> timeEntries = iTimeEntryRepository.readAllByTaskId(taskId);
//        int timeSpentOnTask = TimeCalculator.calculateTimeSpent(timeEntries);
//        task.setTimeSpent(timeSpentOnTask);
//        iTaskRepository.update(task);
//        return timeSpentOnTask;
//    }

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


        for (Task task : tasks) {
            int i = 1;
            System.out.println("Tid brugt på Task med id: " + task.getTaskId() + " er " + task.getTimeSpent());
            timeSpentOnSubProject += task.getTimeSpent();
            System.out.println("Tid brugt på SubProject med id: " + subProjectId + " er nu" + timeSpentOnSubProject + " i loopet på task nummer " + i + "ud af " + tasks.size());
        }

        subProject.setTimeSpent(timeSpentOnSubProject);
        iSubProjectRepository.update(subProject);

        return timeSpentOnSubProject;

        //        // Henter SubProjekt fra subProjectId
//        SubProject subProject = iSubProjectRepository.readById(subProjectId);
//        // Henter listen af Tasks under et SubProject med subProjectId
//        List<Task> tasksUnderSubProject = iTaskRepository.readAllBySubProjectId(subProjectId);
//
//        // timeSpent bliver summeret i et for-each loop ud fra hver Task i listen af Tasks under et SubProject
//        for (Task task : tasksUnderSubProject) {
//            timeSpentOnSubProject += updateTimeSpentForTaskWithId(task.getTaskId());
//        }
//        // Den opsummerede timeSpent-værdi sættes i project og værdien opdateres i databasetabellen
//        subProject.setTimeSpent(timeSpentOnSubProject);
//        iSubProjectRepository.update(subProject);
//        return timeSpentOnSubProject;
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

    //---------------------------- Opdatering med TIMEENTRY -----------------------------

    //---------------------- Opdatering af tidsforbrug hos subtasks ---------------------

    private int updateTimeSpentForSubTask(TimeEntry timeEntry) {
        int timeSpentForSubTask;
        int subTaskId = timeEntry.getSubTaskId();
        SubTask subTask = iSubTaskRepository.readById(subTaskId);
        List<TimeEntry> timeEntries = iTimeEntryRepository.readAllBySubTaskId(subTaskId);

        timeSpentForSubTask = TimeCalculator.calculateTimeSpent(timeEntries);
        subTask.setTimeSpent(timeSpentForSubTask);
        iSubTaskRepository.update(subTask);
        return timeSpentForSubTask;
    }

    //----------------------- Opdatering af tidsforbrug hos tasks -----------------------

    // Metoden tager en TimeEntry og opdaterer timeSpent for den tilhørende task
    private void updateTimeSpentForTask(TimeEntry timeEntry) {
        int totalTimeSpentOnTask = 0;
        // TODO Dette tjek skal jeg ændre på et tidspunkt, for vores H2-testdatabase kører ikke med de samme ID'er som vores normale database x.x
        // Først tjekkes om taskens id findes
        if (timeEntry.getTaskId() > 0) {
            int taskId = timeEntry.getTaskId(); // Henter taskens id fra vores timeEntry-parameter
            Task task = iTaskRepository.readById(taskId); // Henter tasken fra id'et med repository-metode
            if (iSubTaskRepository.readAllByTaskId(taskId).isEmpty()) {
                totalTimeSpentOnTask += updateTimeSpentForTaskWithTimeEntries(timeEntry); // Opdaterer totalTimeSpent-variablens værdi ud fra TimeEntries direkte på tasken. Kan være 0, hvis tid er logget under subtasks i stedet for direkte på tasken
                System.out.println("Listen af subtasks er tom, så vi lægger " + totalTimeSpentOnTask + " til.");
            } else {
                totalTimeSpentOnTask += updateTimeSpentForTaskWithSubTasks(timeEntry); // Opdaterer totalTimeSpent-variablens værdi ud fra timeSpent hos subtasks under givne task
                System.out.println("Listen af subtasks er ikke som, så vi lægger " + totalTimeSpentOnTask + " til.");
            }
            task.setTimeSpent(totalTimeSpentOnTask); // Sætter timeSpent hos tasken til den totale timeSpent
            System.out.println("Vi opdaterer timeSpent til at være " + totalTimeSpentOnTask + "timer.");
            iTaskRepository.update(task);
        }
    }

    // Hjælpemetode til at opsummere timeSpent for en task vha. en TimeEntry
    // Extracted fra updateTimeSpentForTask for bedre læsbarhed
    // Bruger TimeEntries der ligger direkte hos en Task, som ikke har nogle underliggende SubTasks
    private int updateTimeSpentForTaskWithTimeEntries(TimeEntry timeEntry) {
        int timeSpentForTask = 0;
        // Vi gemmer taskens id ud fra TimeEntry-parametren
        int taskId = timeEntry.getTaskId();
        // Vi gemmer en liste af TimeEntries der er logget på denne task
        List<TimeEntry> timeEntries = iTimeEntryRepository.readAllByTaskId(taskId);
        // Hvis der ikke er nogen TimeEntries liggende under
        if (timeEntries.isEmpty()) {
            return timeSpentForTask;
        }
        // Vi sætter timeSpent-attributten hos task-variablen til den udregnede værdi. Dette sker vha. TimeCalculatoren
        timeSpentForTask = TimeCalculator.calculateTimeSpent(timeEntries);
        System.out.println("I update for Task med Time Entry er timeSpentForTask = " + timeSpentForTask);

        return timeSpentForTask;
    }

    // Hjælpemetode til at opsummere timeSpent for en task vha. en TimeEntry
    // Extracted fra updateTimeSpentForTask for bedre læsbarhed
    // Bruger SubTasks under en Task til at opsummere timeSpent for Tasken
    private int updateTimeSpentForTaskWithSubTasks(TimeEntry timeEntry) {
        int timeSpentForTask = 0;
        // Hvis ikke listen af subtasks under den givne task er tom, dvs. der er subtasks under tasken, skal vi et led længere ned
        // Vi laver en lokal Task-variabel ud fra taskId, som bliver taget fra timeEntry-parametren i metoden
        Task task = iTaskRepository.readById(timeEntry.getTaskId());
        // Vi laver en lokal List med SubTasks her for at minimere antallet af repository-operationer (ét kald i stedet for to)
        List<SubTask> subTasks = iSubTaskRepository.readAllByTaskId(timeEntry.getTaskId());
        if (subTasks.isEmpty()) { // Her ville være kald nummer et
            // Hvis listen er tom, så er der kun opdateret tid direkte på vores Task og vi returnerer timeSpentForTask
            System.out.println("I update for Task med SubTasks er timeSpentForTask = " + timeSpentForTask + " når der ikke er nogen subtasks.");
            return timeSpentForTask; // timeSpentForTask er 0
        }
        for (SubTask subTask : subTasks) {// Her ville være kald nummer to
            int subTaskId = subTask.getSubTaskId();
            List<TimeEntry> timeEntries = iTimeEntryRepository.readAllBySubTaskId(subTaskId);
            int timeSpentForSubTask = TimeCalculator.calculateTimeSpent(timeEntries);


            System.out.println("I update for Task med SubTask er timeSpentForTask = " + timeSpentForTask + ", når der er subtasks");
            timeSpentForTask += timeSpentForSubTask;
        }
        // Vi sætter timeSpent-attributten hos tasken der "ejer" subtasken til at være den tidligere værdi
        //  plus den værdi der ligger hos subtasken
        task.setTimeSpent(timeSpentForTask); // TODO Skal fikses på et tidspunkt så den også tager højde for, at tid sættes ned

        return timeSpentForTask;
    }

    //-------------------- Opdatering af tidsforbrug hos subprojekter -------------------

    //                           --- SUBPROJEKT M. TIMEENTRY ---
    // Metoden tager en TimeEntry og opdaterer det subprojekt, som den TimeEntry er logget under
    private void updateTimeSpentForSubProject(TimeEntry timeEntry) {
        // Henter taskId fra timeEntry
        int taskId = timeEntry.getTaskId();
        // Henter Task fra taskId så vi kan hente subProjectId fra task
        Task taskForGettingSubProjectId = iTaskRepository.readById(taskId);
        // Henter subProjectId fra task
        int subProjectId = taskForGettingSubProjectId.getSubProjectId();
        // Kalder ovenstående update-metode for subprojekter vha. subprojekt-id
        updateTimeSpentForSubProjectWithId(subProjectId);
    }

    //--------------------- Opdatering af tidsforbrug hos projekter ---------------------

    //                            --- SUBPROJEKT M. TIMEENTRY ---
    // Metoden tager en TimeEntry og opdaterer det projekt, som den TimeEntry er logget under
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
    }
}
