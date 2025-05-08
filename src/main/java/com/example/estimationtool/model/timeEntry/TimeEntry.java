package com.example.estimationtool.model.timeEntry;

import java.time.LocalDate;

public class TimeEntry {
    private int timeId, userId, taskId, subTaskId, hoursSpent;
    private LocalDate date;

    public TimeEntry(int timeId, int userId, int taskId,
                     int subTaskId, int hoursSpent, LocalDate date) {
        setTimeId(timeId);
        setUserId(userId);
        setTaskId(taskId);
        setSubTaskId(subTaskId);
        setHoursSpent(hoursSpent);
        setDate(date);
    }

    // Getter-metoder

    public int getTimeId() {
        return timeId;
    }

    public int getUserId() {
        return userId;
    }

    public int getTaskId() {
        return taskId;
    }

    public int getSubTaskId() {
        return subTaskId;
    }

    public int getHoursSpent() {
        return hoursSpent;
    }
    public LocalDate getDate() {
        return date;
    }

    // Setter-metoder

    public void setTimeId(int timeId) {
        this.timeId = timeId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public void setSubTaskId(int subTaskId) {
        this.subTaskId = subTaskId;
    }

    public void setHoursSpent(int hoursSpent) {
        this.hoursSpent = hoursSpent;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return String.format("""
                Time ID: %d
                User ID: %d
                Task ID: %d
                SubTask ID: %d
                Hours spent: %d
                Date of entry: %d
                """, timeId, userId, taskId, subTaskId, timeSpent, date);
    }
}
