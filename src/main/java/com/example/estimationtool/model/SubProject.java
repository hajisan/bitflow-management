package com.example.estimationtool.model;

import com.example.estimationtool.model.enums.Status;

import java.time.LocalDate;

public class SubProject implements Estimable {


    private int subProjectId, projectId, estimatedTime, timeSpent;
    private String name, description;
    private LocalDate deadline;
    private Status status;

    // Tom constructor
    public SubProject() {
    }

    // Overloaded constructor der instantierer alle felter med parametre
    public SubProject(int subProjectId, int projectId, int estimatedTime, int timeSpent,
                      String name, String description, LocalDate deadline,
                      Status status) {
        setSubProjectId(subProjectId);
        setProjectId(projectId);
        setEstimatedTime(estimatedTime);
        setTimeSpent(timeSpent);
        setName(name);
        setDescription(description);
        setDeadline(deadline);
        setStatus(status);
    }

    // Overloaded constructor til at opdatere et subprojekt
    public SubProject(int projectId, int estimatedTime, int timeSpent,
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

    // Overloaded constructor der ikke sætter subprojektets id og estimatedTime
    public SubProject(int projectId, int estimatedTime,
                      String name, String description, LocalDate deadline,
                      Status status) {
        setProjectId(projectId);
        setEstimatedTime(estimatedTime);
        setTimeSpent(0);
        setName(name);
        setDescription(description);
        setDeadline(deadline);
        setStatus(status);
    }

    // Getter-metoder

    public int getSubProjectId() {
        return subProjectId;
    }

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

    public void setSubProjectId(int subProjectId) {
        this.subProjectId = subProjectId;
    }

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
            SubProject ID : %d
            Project ID    : %d
            Name          : %s
            Description   : %s
            Estimated     : %d
            Time Spent    : %d
            Deadline      : %s
            Status        : %s
            """, subProjectId, projectId, name, description, estimatedTime, timeSpent, deadline, status);
    }
}