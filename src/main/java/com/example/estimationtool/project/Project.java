package com.example.estimationtool.project;

import com.example.estimationtool.enums.Status;

import java.time.LocalDate;

public class Project {


    private int projectId, userId, estimatedTime, timeSpent;
    private String name, description;
    private LocalDate localDate;
    private Status status;

    public Project(int projectId, int userId, int estimatedTime, int timeSpent,
                   String name, String description, LocalDate localDate,
                   Status status) {
        setProjectId(projectId);
        setUserId(userId);
        setEstimatedTime(estimatedTime);
        setTimeSpent(timeSpent);
        setName(name);
        setDescription(description);
        setLocalDate(localDate);
        setStatus(status);
    }
    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(int estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public int getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(int timeSpent) {
        this.timeSpent = timeSpent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String toString() {
        return String.format("""
                Project ID: %d
                User ID: %d
                Name: %s
                Description: %s
                Estimated Time: %d
                Time spent: %d
                Local date: %d
                Status: %d
                """, projectId, userId, name, description, estimatedTime, timeSpent, localDate, status);
    }





}
