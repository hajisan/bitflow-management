package com.example.estimationtool.model;

import com.example.estimationtool.enums.Status;

import java.time.LocalDate;

public class Task {
    private int subProjectId, taskId, estimatedTime;
    private String name, description;
    private LocalDate deadline;
    private Status status;

    public Task(int subProjectId, int taskId, int estimatedTime,
                String name, String description, LocalDate deadline,
                Status status) {
        setSubProjectId(subProjectId);
        setTaskId(taskId);
        setEstimatedTime(estimatedTime);
        setName(name);
        setDescription(description);
        setDeadline(deadline);
        setStatus(status);

    }
    public Task() {}

    // Getter-metoder

    public int getSubProjectId() {
        return subProjectId;
    }
    public int getTaskId() {
        return taskId;
    }

    public int getEstimatedTime() {
        return estimatedTime;
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

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public void setEstimatedTime(int estimatedTime) {
        this.estimatedTime = estimatedTime;
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
                Task ID: %d
                SubProject ID: %d
                Name: %s
                Description: %s
                Estimated Time: %d
                Deadline: %d
                Status: %d
                """, taskId, subProjectId, name, description, estimatedTime, deadline, status);
    }
}
