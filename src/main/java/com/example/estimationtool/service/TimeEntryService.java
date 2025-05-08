package com.example.estimationtool.service;

import com.example.estimationtool.model.timeEntry.TimeEntry;
import com.example.estimationtool.repository.interfaces.ITimeEntryRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TimeEntryService {

    private final ITimeEntryRepository iTimeEntryRepository;

    public TimeEntryService(ITimeEntryRepository iTimeEntryRepository) {
        this.iTimeEntryRepository = iTimeEntryRepository;
    }


    //------------------------------------ Create() ------------------------------------

    public TimeEntry createTimeEntry(TimeEntry timeEntry) {

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
        return iTimeEntryRepository.update(timeEntry);
    }
    //------------------------------------ Delete() ------------------------------------

    public void deleteById(int id) {

        iTimeEntryRepository.deleteById(id);
    }
}
