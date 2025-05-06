package com.example.estimationtool.subProject;

import com.example.estimationtool.enums.Status;

import java.time.LocalDate;

public class SubProject {


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

    // Overloaded constructor der instantierer alle felter med parametre - UNDTAGEN timeSpent, da den er 0,
    // når et nyt subprojekt oprettes
    public SubProject(int subProjectId, int projectId, int estimatedTime,
                      String name, String description, LocalDate deadline,
                      Status status) {
        setSubProjectId(subProjectId);
        setProjectId(projectId);
        setEstimatedTime(estimatedTime);
        setTimeSpent(0);
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

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
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
                ", projectId=" + projectId +
                ", estimatedTime=" + estimatedTime +
                ", timeSpent=" + timeSpent +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", localDate=" + deadline +
                ", status=" + status +
                '}';
    }
}