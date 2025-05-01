package com.example.estimationtool.task;

import com.example.estimationtool.enums.Status;

import java.time.LocalDate;

public class Task {
    private int subTaskId, taskId, estimatedTime;
    private String name, description;
    private LocalDate deadline;
    private Status status;

    public Task(int subTaskId, int taskId, int estimatedTime,
                String name, String description, LocalDate deadline,
                Status status) {
        setSubTaskId(subTaskId);
        setTaskId(taskId);
        setEstimatedTime(estimatedTime);
        setName(name);
        setDescription(description);
        setLocalDate(deadline);
        setStatus(status);

    }

    public int getSubTaskId() {
        return subTaskId;
    }

    public void setSubTaskId(int subTaskId) {
        this.subTaskId = subTaskId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(int estimatedTime) {
        this.estimatedTime = estimatedTime;
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

    public void setLocalDate(LocalDate deadline) {
        this.deadline = deadline;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String toString() {
        return String.format("""
                Task ID: %d
                SubTask ID: %d
                Name: %s
                Description: %s
                Estimated Time: %d
                Deadline: %d
                Status: %d
                """, taskId, subTaskId, name, description, estimatedTime, deadline, status);
    }
}
