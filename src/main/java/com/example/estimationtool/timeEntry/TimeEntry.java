package com.example.estimationtool.timeEntry;

import java.time.LocalDate;

public class TimeEntry {
    private int timeId, userId, taskId, subTaskId, timeSpent;
    private LocalDate date;

    public TimeEntry(int timeId, int userId, int taskId, int subTaskId, int timeSpent, LocalDate date) {
        setTimeId(timeId);
        setUserId(userId);
        setTaskId(taskId);
        setSubTaskId(subTaskId);
        setTimeSpent(timeSpent);
        setDate(date);
    }

    public int getTimeId() {
        return timeId;
    }

    public void setTimeId(int timeId) {
        this.timeId = timeId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getSubTaskId() {
        return subTaskId;
    }

    public void setSubTaskId(int subTaskId) {
        this.subTaskId = subTaskId;
    }

    public int getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(int timeSpent) {
        this.timeSpent = timeSpent;
    }

    public LocalDate getDate() {
        return date;
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
                Time Spent (hour): %d
                Date of entry: %d
                """, timeId, userId, taskId, subTaskId, timeSpent, date);
    }
}
