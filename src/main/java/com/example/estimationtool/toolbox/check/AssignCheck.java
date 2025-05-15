package com.example.estimationtool.toolbox.check;

import com.example.estimationtool.model.Project;
import com.example.estimationtool.model.SubProject;
import com.example.estimationtool.model.SubTask;
import com.example.estimationtool.model.Task;
import com.example.estimationtool.toolbox.dto.UserWithProjectsDTO;
import com.example.estimationtool.toolbox.dto.UserWithSubProjectsDTO;
import com.example.estimationtool.toolbox.dto.UserWithSubTasksDTO;
import com.example.estimationtool.toolbox.dto.UserWithTasksDTO;

// Tjekker om bruger er tilknyttet en bestemt model
// DTO'erne indeholder brugerens tildelte modeller

public class AssignCheck {



    // ------------------------------------ User med Projects ------------------------------------------

    public static void ensureUserAssignedToProject(UserWithProjectsDTO userWithProjectsDTO, int projectId) {

        for (Project project : userWithProjectsDTO.projectList()) {
            if (project.getProjectId() == projectId) {
                return;
            }
        }
        throw new SecurityException("Status for projektet kan kun ændres af tilknyttede brugere.");
    }

    // ------------------------------------ User med SubProjects ---------------------------------------

    public static void ensureUserAssignedToSubProject(UserWithSubProjectsDTO userWithSubProjectsDTO, int subProjectId) {

        for (SubProject subProject : userWithSubProjectsDTO.subProjectList()) {
            if (subProject.getSubProjectId() == subProjectId) {
                return;
            }
        }
        throw new SecurityException("Status for subprojektet kan kun ændres af tilknyttede brugere.");
    }

    // ------------------------------------ User med Tasks ------------------------------------------

    public static void ensureUserAssignedToTask(UserWithTasksDTO userWithTasksDTO, int taskId) {

        for (Task task : userWithTasksDTO.taskList()) {
            if (task.getTaskId() == taskId) {
                return;
            }
        }
        throw new SecurityException("Status for task kan kun ændres af tilknyttede brugere.");
    }

    // ------------------------------------ User med SubTasks ----------------------------------------

    public static void ensureUserAssignedToSubTask(UserWithSubTasksDTO userWithSubTasksDTO, int subTaskId) {

        for (SubTask subTask : userWithSubTasksDTO.subTaskList()) {
            if (subTask.getSubTaskId() == subTaskId) {
                return;
            }
        }
        throw new SecurityException("Status for subtask kan kun ændres af tilknyttede brugere.");
    }


}
