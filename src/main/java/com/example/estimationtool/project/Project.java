package com.example.estimationtool.project;

import com.example.estimationtool.enums.Status;

import java.time.LocalDate;

public class Project {


    private int projectId, userId, estimatedTime, timeSpent;
    private String name, description;
    private LocalDate deadline;
    private Status status;

    public Project(int projectId, int userId, int estimatedTime, int timeSpent,
                   String name, String description, LocalDate deadline,
                   Status status) {
        setProjectId(projectId);
        setUserId(userId);
        setEstimatedTime(estimatedTime);
        setTimeSpent(timeSpent);
        setName(name);
        setDescription(description);
        setDeadLine(deadline);
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

    public LocalDate getDeadLine() {
        return deadline;
    }

    public void setDeadLine(LocalDate deadline) {
        this.deadline = deadline;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }


    @Override
    public String toString() {
        return "Project{" +
                "projectId=" + projectId +
                ", userId=" + userId +
                ", estimatedTime=" + estimatedTime +
                ", timeSpent=" + timeSpent +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", localDate=" + deadline +
                ", status=" + status +
                '}';
    }



}
