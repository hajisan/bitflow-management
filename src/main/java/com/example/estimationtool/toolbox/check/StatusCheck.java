package com.example.estimationtool.toolbox.check;

import com.example.estimationtool.model.SubProject;
import com.example.estimationtool.model.SubTask;
import com.example.estimationtool.model.Task;
import com.example.estimationtool.model.enums.Status;
import com.example.estimationtool.toolbox.dto.ProjectWithSubProjectsDTO;
import com.example.estimationtool.toolbox.dto.SubProjectWithTasksDTO;
import com.example.estimationtool.toolbox.dto.TaskWithSubTasksDTO;
import org.springframework.stereotype.Component;

import java.util.List;

// @Component = hjælpeklasse

@Component // Så Spring kan oprette instans af klassen (@Bean), som nu kan injiceres i Service-laget
public class StatusCheck {

    // ------------------------------- Projekt med SubProjekter ---------------------------------

    public boolean canMarkProjectAsDone(ProjectWithSubProjectsDTO projectWithSubProjectsDTO) {

        List<SubProject> subProjects = projectWithSubProjectsDTO.subProjectList();
        for (SubProject subProject : subProjects) {
            if (subProject.getStatus() != Status.DONE) {
                return false;
            }
        }
        return true;
    }

    // ------------------------------- SubProjekt med Task --------------------------------------

    public boolean canMarkSubProjectAsDone(SubProjectWithTasksDTO subProjectWithTasksDTO) {

        List<Task> tasks = subProjectWithTasksDTO.taskList();
        for (Task task : tasks) {
            if (task.getStatus() != Status.DONE) {
                return false;
            }
        }
        return true;
    }

    // ------------------------------- Task med SubTasks ----------------------------------------

    public boolean canMarkTaskAsDone(TaskWithSubTasksDTO taskWithSubTasksDTO) {

        List<SubTask> subTasks = taskWithSubTasksDTO.subTaskList();

        for (SubTask subTask : subTasks) {
            if(subTask.getStatus() != Status.DONE) {
                return false;
            }
        }
        return true;
    }

    // ------------------------------- SubTask --------------------------------------------------

    public boolean canMarkSubTaskAsDone(SubTask subTask) {

        return true;
    }
}
