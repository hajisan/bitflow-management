package com.example.estimationtool.subProject;

import com.example.estimationtool.enums.Status;

import java.time.LocalDate;

public class SubProject {


    private int subProjectId, userId, estimatedTime, timeSpent;
    private String name, description;
    private LocalDate deadline;
    private Status status;

    public SubProject(int subProjectId, int userId, int estimatedTime, int timeSpent,
                      String name, String description, LocalDate deadline,
                      Status status) {
        setSubProjectId(subProjectId);
        setUserId(userId);
        setEstimatedTime(estimatedTime);
        setTimeSpent(timeSpent);
        setName(name);
        setDescription(description);
        setDeadline(deadline);
        setStatus(status);
    }

    public int getSubProjectId() {
        return subProjectId;
    }

    public void setSubProjectId(int subProjectId) {
        this.subProjectId = subProjectId;
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

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
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
        return "SubProject{" +
                "subProjectId=" + subProjectId +
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