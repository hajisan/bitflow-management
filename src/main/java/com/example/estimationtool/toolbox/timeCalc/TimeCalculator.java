package com.example.estimationtool.toolbox.timeCalc;

import com.example.estimationtool.model.Project;
import com.example.estimationtool.model.SubProject;
import com.example.estimationtool.model.SubTask;
import com.example.estimationtool.model.Task;
import com.example.estimationtool.model.timeEntry.TimeEntry;
import com.example.estimationtool.repository.interfaces.*;
import org.springframework.stereotype.Component;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Component
public class TimeCalculator {
    private final ISubTaskRepository iSubTaskRepository;
    private final ITaskRepository iTaskRepository;
    private final ISubProjectRepository iSubProjectRepository;
    private final IProjectRepository iProjectRepository;
    private final ITimeEntryRepository iTimeEntryRepository;

    public TimeCalculator(
            ISubTaskRepository iSubTaskRepository,
            ITaskRepository iTaskRepository,
            ISubProjectRepository iSubProjectRepository,
            IProjectRepository iProjectRepository,
            ITimeEntryRepository iTimeEntryRepository) {

        this.iSubTaskRepository = iSubTaskRepository;
        this.iTaskRepository = iTaskRepository;
        this.iSubProjectRepository = iSubProjectRepository;
        this.iProjectRepository = iProjectRepository;
        this.iTimeEntryRepository = iTimeEntryRepository;
    }

    //------------- Opdatering af tidsforbrug hos alle projektkomponenter --------------

    // På grund af den hierarkiske struktur i vores modeller (SubTask har reference til ovenstående Tasks ID osv.)
    // opdaterer metoden timeSpent hos komponenterne fra bunden af hierarkiet og opad igennem
    public void updateTimeSpentForAll(TimeEntry timeEntry) {
        // Hvis der er registreret en subtask på den givne TimeEntry, skal vi regne den med:
        if (timeEntry.getSubTaskId() > 0) {
            int subTaskId = timeEntry.getSubTaskId();                                       // Henter subTaskId fra timeEntry
            updateTimeSpentForSubTaskWithId(subTaskId);                                     // Opdaterer SubTask med subTaskId
            int taskId = iSubTaskRepository.readById(subTaskId).getTaskId();                // Henter taskId fra SubTask, som er hentet fra sit eget subTaskId
            updateTimeSpentForTaskWithId(taskId);                                           // Opdaterer Task med taskId
            int subProjectId = iTaskRepository.readById(taskId).getSubProjectId();          // Henter subProjectId fra Task, som er hentet fra sit eget taskId
            updateTimeSpentForSubProjectWithId(subProjectId);                               // Opdaterer SubProject med subProjectId
            int projectId = iSubProjectRepository.readById(subProjectId).getProjectId();    // Henter projectId fra subProject, som er hentet fra sit eget subProjectId
            updateTimeSpentForProjectWithId(projectId);                                     // Opdaterer Project med projectId
        }
        // Hvis der ikke er registreret en subtask, springer vi det trin over og kører således:
        else if (timeEntry.getTaskId() > 0) {
            int taskId = timeEntry.getTaskId();                                             // Henter taskId fra timeEntry
            updateTimeSpentForTaskWithId(taskId);                                           // Opdaterer Task med taskId
            int subProjectId = iTaskRepository.readById(taskId).getSubProjectId();          // Henter subProjectId fra Task, som er hentet fra sit eget taskId
            updateTimeSpentForSubProjectWithId(subProjectId);                               // Opdaterer SubProject med subProjectId
            int projectId = iSubProjectRepository.readById(subProjectId).getProjectId();    // Henter projectId fra subProject, som er hentet fra sit eget subProjectId
            updateTimeSpentForProjectWithId(projectId);                                     // Opdaterer Project med projectId
        }
    }

    //---------------- Opdatering af tidsforbrug hos projektkomponenter -----------------

    //----------------- Opdatering af tidsforbrug hos tasks og subtasks -----------------

    //                                  --- SUBTASK M. ID ---
    // Metoden tager et subtask-id og opdaterer timeSpent for den givne subtask
    public int updateTimeSpentForSubTaskWithId(int subTaskId) {
        // Opretter en lokal SubTask-instans ud fra parametren til at opdatere timeSpent hos
        SubTask subTask = iSubTaskRepository.readById(subTaskId);

        // Henter listen af TimeEntries under et SubProject med subTaskId
        List<TimeEntry> timeEntries = iTimeEntryRepository.readAllBySubTaskId(subTaskId);

        // Opretter lokal variabel til at opbevare det summerede tidsforbrug på subtasken
        int timeSpentOnSubTask = calculateTimeSpent(timeEntries);

        // timeSpent-attributten bliver sat hos den lokale instans og opdateret med den tilsvarende repository-update-metode
        subTask.setTimeSpent(timeSpentOnSubTask);
        iSubTaskRepository.update(subTask);

        // Den totale timeSpent returneres for testbarhed og evt. udvidelser af programmet
        return timeSpentOnSubTask;
    }

    //                                    --- TASK M. ID ---
    // Metoden tager et task-id og opdaterer timeSpent for den givne task
    public int updateTimeSpentForTaskWithId(int taskId) {
        // Opretter en lokal Task-instans ud fra parametren til at opdatere timeSpent hos
        Task task = iTaskRepository.readById(taskId);

        // Henter listen af Tasks under et SubProject med subProjectId
        List<SubTask> subTasks = iSubTaskRepository.readAllByTaskId(task.getTaskId());

        // Opretter lokal variabel til at opbevare det summerede tidsforbrug på tasken
        int timeSpentOnTask = 0;

        // Først tjekkes der om vi er nået til bunds i hierarkiet. Hvis listen af SubTasks er tom, så er Tasken nederst.
        if (subTasks.isEmpty()) {
            // Da Tasken er nederst i hierarkiet, bør dens timeSpent blive beregnet direkte ud fra sine TimeEntries
            List<TimeEntry> timeEntries = iTimeEntryRepository.readAllByTaskId(taskId);
            // Der udregnes med calculateTimeSpent-metoden fra TimeCalculator
            timeSpentOnTask = calculateTimeSpent(timeEntries);
        }
        // Ellers er vi ikke nederst i hierarkiet og vi udregner timeSpent fra SubTasks
        else {
            // timeSpent bliver summeret i et for-each loop ud fra hvert SubTask i listen af SubTasks under en Task
            for (SubTask subTask : subTasks) {
                timeSpentOnTask += subTask.getTimeSpent();
            }
        }

        // timeSpent-attributten bliver sat hos den lokale instans og opdateret med den tilsvarende repository-update-metode
        task.setTimeSpent(timeSpentOnTask);
        iTaskRepository.update(task);

        // Den totale timeSpent returneres for testbarhed og evt. udvidelser af programmet
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

        // timeSpent bliver summeret i et for-each loop ud fra hvert Task i listen af Tasks under et SubProject
        for (Task task : tasks) {
            timeSpentOnSubProject += task.getTimeSpent();
        }

        // timeSpent-attributten bliver sat hos den lokale instans og opdateret med den tilsvarende repository-update-metode
        subProject.setTimeSpent(timeSpentOnSubProject);
        iSubProjectRepository.update(subProject);

        // Den totale timeSpent returneres for testbarhed og evt. udvidelser af programmet
        return timeSpentOnSubProject;
    }

    //-------------------- Opdatering af tidsforbrug hos projekter -----------------------

    //                               --- PROJEKT M. ID ---
    // Metoden tager et projekt-id og opdaterer timeSpent hos det givne projekt
    public int updateTimeSpentForProjectWithId(int projectId) {
        // Opretter en lokal Project-instans ud fra parametren til at opdatere timeSpent hos
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

        // Den totale timeSpent returneres for testbarhed og evt. udvidelser af programmet
        return timeSpentOnProject;
    }

    //------------------------------- Andre metoder til tidsberegning -------------------------------
    //------------------------------------------ Tid Brugt ------------------------------------------

    public int calculateTimeSpent(List<TimeEntry> entries) {
        // Opretter en lokal variabel til at gemme den akkumulerede timeSpent
        int totalTimeSpent = 0;
        // Summerer timeSpent for hver TimeEntry i listen og gemmer det i totalTimeSpent
        for (TimeEntry timeEntry : entries) {
            totalTimeSpent += timeEntry.getHoursSpent();
        }
        // Returnerer totalTimeSpent for alle TimeEntries i listen
        return totalTimeSpent;
    }

    //------------------------------------ Tid tilbage i TIMER ------------------------------------

    public double calculateRemainingTimeInHours(LocalDate deadline) {
        return countWorkDaysLeft(deadline) * (37 / 5.0); // Vi antager at en arbejdsuge er 37 timer med 5 hverdage
    }

    //---------------------------- Tid i TIMER pr. DAG m. hjælpemetode ----------------------------

    public double calculateHoursNeededPerDay(int remainingHours, LocalDate deadline) {
        // until-metoden hos LocalDate returnerer longs og ikke ints, hvilket er grunden til at værdien bliver castet
        double daysUntilDeadline = countWorkDaysLeft(deadline); // Se metoden countWorkDays
        if (daysUntilDeadline > 0) { // Sørger for at vi er før deadlinen, samtidig med at undgå at dividere med 0
            return (double) remainingHours / daysUntilDeadline; // Returnerer timer pr. dag til deadline
        } else if (daysUntilDeadline == 0) return remainingHours; // Hvis daysUntilDeadline er 0, så er vi på
        return 0; // TODO Skal vi bruge en exception her i stedet?
    }

    private double countWorkDaysLeft(LocalDate deadline) {
        double workingDays = 0.0;                                       // Vi starter med 0 hverdage og inkrementerer hver gang vi møder en hverdag i while-loopet
        LocalDate dateToCheck = LocalDate.now();                        // Den dato vi tjekker - vi starter med at tjekke i dag

        while (dateToCheck.isBefore(deadline)) {                        // Så længe den dato vi tjekker er før deadlinen kører loopet
            DayOfWeek day = dateToCheck.getDayOfWeek();                 // Vi henter den dag på ugen vi har på den dato vi vil tjekke
            if (day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY) { // Hvis det ikke er lørdag og ikke er søndag
                workingDays++;                                          // tilføjer vi en hverdag til vores variabel
            }
            dateToCheck = dateToCheck.plusDays(1);                      // Vi går videre til næste dato der skal tjekkes ved at tilføje en dag til datoen
        }
        return workingDays;
    }

    public int countDevsNeeded(int remainingHours, LocalDate deadline) {
        double hoursPerDay = calculateHoursNeededPerDay(remainingHours, deadline);
        return (int) hoursPerDay / 8; // En arbejdsuge på 5 dage og 37 timer giver 7,4 timer pr. dag. Vi under op til 8.
    }
}