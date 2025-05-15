package com.example.estimationtool.service;

import com.example.estimationtool.model.SubTask;
import com.example.estimationtool.model.Task;
import com.example.estimationtool.model.timeEntry.TimeEntry;

import com.example.estimationtool.repository.interfaces.ISubTaskRepository;
import com.example.estimationtool.repository.interfaces.ITaskRepository;
import com.example.estimationtool.repository.interfaces.ITimeEntryRepository;

import com.example.estimationtool.toolbox.timeCalc.TimeCalculator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimeEntryService {

    private final ITimeEntryRepository iTimeEntryRepository;
    private final ISubTaskRepository iSubTaskRepository;
    private final ITaskRepository iTaskRepository;

    public TimeEntryService(ITimeEntryRepository iTimeEntryRepository, ISubTaskRepository iSubTaskRepository, ITaskRepository iTaskRepository) {
        this.iTimeEntryRepository = iTimeEntryRepository;
        this.iSubTaskRepository = iSubTaskRepository;
        this.iTaskRepository = iTaskRepository;
    }

    //------------------------------------ Create() ------------------------------------

    public TimeEntry createTimeEntry(TimeEntry timeEntry) {
        updateTimeSpent(timeEntry);
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
        updateTimeSpent(timeEntry);
        return iTimeEntryRepository.update(timeEntry);
    }
    //------------------------------------ Delete() ------------------------------------

    public void deleteById(int id) {

        iTimeEntryRepository.deleteById(id);
    }

    //----------------- Opdatering af tidsforbrug hos tasks og subtasks -----------------

    public void updateTimeSpent(TimeEntry timeEntry) {
        // TODO Dette tjek skal jeg ændre på et tidspunkt, for vores H2-testdatabase kører ikke med de samme ID'er som vores normale database x.x
        if (timeEntry.getTaskId() > 0) {
            List<SubTask> subTasks = iSubTaskRepository.readAllByTaskId(timeEntry.getTaskId());
            if (subTasks.isEmpty()) {
                int taskId = timeEntry.getTaskId();
                Task task = iTaskRepository.readById(timeEntry.getTaskId());
                List<TimeEntry> timeEntries = iTimeEntryRepository.readAllByTaskId(taskId);
                task.setTimeSpent(TimeCalculator.calculateTimeSpent(timeEntries));
                iTaskRepository.update(task);
            } else {
                if (timeEntry.getSubTaskId() > 0) {
                    for (SubTask subTask : iSubTaskRepository.readAllByTaskId(timeEntry.getTaskId())) {
                        int subTaskId = timeEntry.getSubTaskId();
                        //SubTask subTask = iSubTaskRepository.readById(subTaskId);
                        List<TimeEntry> timeEntries = iTimeEntryRepository.readAllBySubTaskId(subTaskId);
                        subTask.setTimeSpent(TimeCalculator.calculateTimeSpent(timeEntries));
                        iSubTaskRepository.update(subTask);
                        //iTaskRepository.update(iTaskRepository.readById(subTask.getTaskId()));
                    }
                }
            }
        }
    }
}
