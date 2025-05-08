package com.example.estimationtool.service;

import com.example.estimationtool.model.timeEntry.TimeEntry;
import com.example.estimationtool.repository.interfaces.ITimeEntryRepository;
import org.springframework.stereotype.Service;

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

    //------------------------------------ Update() ------------------------------------

    //------------------------------------ Delete() ------------------------------------

}
