package com.killeen.taskflow.components.tasklist.exception;

public class TaskListNotFoundException extends RuntimeException {

    public TaskListNotFoundException(String message) {
        super(message);
    }
}
