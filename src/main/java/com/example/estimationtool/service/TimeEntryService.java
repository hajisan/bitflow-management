package com.example.estimationtool.service;

import com.example.estimationtool.model.timeEntry.TimeEntry;
import com.example.estimationtool.repository.interfaces.*;
import com.example.estimationtool.toolbox.timeCalc.TimeCalculator;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimeEntryService {

    private final ITimeEntryRepository iTimeEntryRepository;
    private final TimeCalculator timeCalculator;

    public TimeEntryService(ITimeEntryRepository iTimeEntryRepository, TimeCalculator timeCalculator) {
        this.iTimeEntryRepository = iTimeEntryRepository;
        this.timeCalculator = timeCalculator;
    }

    //------------------------------------ Create() ------------------------------------

    public TimeEntry createTimeEntry(TimeEntry timeEntry) {
        TimeEntry createdTimeEntry = iTimeEntryRepository.create(timeEntry);
        timeCalculator.updateTimeSpentForAll(timeEntry);
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
        timeCalculator.updateTimeSpentForAll(timeEntry);
        // Sørger for at kalde repository-CRUD-metoden før opdateringen af timeSpent
        return updatedTimeEntry;
    }

    //------------------------------------ Delete() ------------------------------------

    public void deleteById(int id) {
        iTimeEntryRepository.deleteById(id);
    }
}
