package com.example;

import com.example.common.QueueConfig;
import com.example.common.QueueMessage;

import java.util.concurrent.CompletableFuture;

public class FileQueueService implements QueueService {
    public CompletableFuture<String> createQueue(QueueConfig queueConfig) {
        return null;
    }

    public CompletableFuture<Void> sendMessage(String queueId, QueueMessage queueMessage) {
        return null;
    }

    public CompletableFuture<QueueMessage> retrieveMessage(String queueId) {
        return null;
    }

    public CompletableFuture<Void> deleteMessage(String queueId, QueueMessage queueMessage) {
        return null;
    }

    public CompletableFuture<String> removeQueue(String queueId) {
        return null;
    }
}
