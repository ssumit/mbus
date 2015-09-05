package com.example;

public class QueueNotFoundException extends RuntimeException {
    public static final String QUEUE_NOT_FOUND = "Queue not found";

    public QueueNotFoundException(String message) {
        super(QUEUE_NOT_FOUND);
    }
}
