package com.example.estimationtool.model;

import com.example.estimationtool.model.enums.Status;
import org.springframework.cglib.core.Local;

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

    // Konstruktør til Admin, som ikke har brug for et userId, for at undgå duplikater
    public Project(int projectId, int estimatedTime, int timeSpent,
                   String name, String description, LocalDate deadline,
                   Status status) {
        setProjectId(projectId);
        setEstimatedTime(estimatedTime);
        setTimeSpent(timeSpent);
        setName(name);
        setDescription(description);
        setDeadLine(deadline);
        setStatus(status);
    }

    // Tom konstruktør til integrationstest
    public Project() {

    }

// Getter-metoder

    public int getProjectId() {
        return projectId;
    }
    public int getUserId() {
        return userId;
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
    public LocalDate getDeadLine() {
        return deadline;
    }
    public Status getStatus() {
        return status;
    }

// Setter-metoder

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
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
    public void setDeadLine(LocalDate deadline) {
        this.deadline = deadline;
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
