package com.example.estimationtool.model;

import com.example.estimationtool.model.enums.Status;

import java.time.LocalDate;

public class Project{

    private int projectId, estimatedTime, timeSpent;
    private String name, description;
    private LocalDate deadline;
    private Status status;

    public Project(int projectId, int estimatedTime, int timeSpent,
                   String name, String description, LocalDate deadline,
                   Status status) {
        setProjectId(projectId);
        setEstimatedTime(estimatedTime);
        setTimeSpent(timeSpent);
        setName(name);
        setDescription(description);
        setDeadline(deadline);
        setStatus(status);
    }

    // Konstruktør til Admin, som ikke har brug for et userId, for at undgå duplikater
//    public Project(int projectId, int estimatedTime, int timeSpent,
//                   String name, String description, LocalDate deadline,
//                   Status status) {
//        setProjectId(projectId);
//        setEstimatedTime(estimatedTime);
//        setTimeSpent(timeSpent);
//        setName(name);
//        setDescription(description);
//        setDeadline(deadline);
//        setStatus(status);
//    }

    // Tom konstruktør til integrationstest
    public Project() {

    }

// Getter-metoder

    public int getProjectId() {
        return projectId;
    }
    public int getEstimatedTime() {
        return estimatedTime;
    }
    public int getTimeSpent() {
        return timeSpent;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public LocalDate getDeadline() {
        return deadline;
    }
    public Status getStatus() {
        return status;
    }

// Setter-metoder

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }
    public void setEstimatedTime(int estimatedTime) {
        this.estimatedTime = estimatedTime;
    }
    public void setTimeSpent(int timeSpent) {
        this.timeSpent = timeSpent;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }
    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("""
            Project ID   : %d
            Name         : %s
            Description  : %s
            Estimated    : %d
            Time Spent   : %d
            Deadline     : %s
            Status       : %s
            """, projectId, name, description, estimatedTime, timeSpent, deadline, status);
    }
}
