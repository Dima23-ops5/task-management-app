package com.example.task_management_app.exception;

public class EmailSendingException extends RuntimeException {
    public EmailSendingException(String message, Throwable e) {
        super(message, e);
    }
}
