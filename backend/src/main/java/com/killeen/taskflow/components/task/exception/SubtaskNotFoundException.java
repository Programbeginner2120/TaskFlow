package com.killeen.taskflow.components.task.exception;

public class SubtaskNotFoundException extends RuntimeException {

    public SubtaskNotFoundException(String message) {
        super(message);
    }
}
