package com.example.memQueue;

import com.example.QueueNotFoundException;
import com.example.common.QueueConfig;
import com.example.common.QueueMessage;
import com.example.QueueService;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This service is light weight so working on same thread as that of the invoker
 */
public class InMemoryQueueService implements QueueService {
    private final Map<String, MemoryQueue> queueMap;

    public InMemoryQueueService() {
        queueMap = new ConcurrentHashMap<String, MemoryQueue>();
    }

    public CompletableFuture<String> createQueue(QueueConfig queueConfig) {
        MemoryQueue memoryQueue = new MemoryQueue(queueConfig);
        String uniqueId = UUID.randomUUID().toString();
        queueMap.put(uniqueId, memoryQueue);
        return CompletableFuture.completedFuture(uniqueId);
    }

    public CompletableFuture<Void> sendMessage(String queueId, QueueMessage queueMessage) {
        CompletableFuture<Void> future = new CompletableFuture<Void>();
        if (queueMap.containsKey(queueId)) {
            try {
                queueMap.get(queueId).addMessage(queueMessage);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        } else {
            future.completeExceptionally(new QueueNotFoundException());
        }
        return future;
    }

    public CompletableFuture<QueueMessage> retrieveMessage(String queueId) {
        CompletableFuture<QueueMessage> future = new CompletableFuture<QueueMessage>();
        if (queueMap.containsKey(queueId)) {
            future.complete(queueMap.get(queueId).getMessage());
        } else {
            future.completeExceptionally(new QueueNotFoundException());
        }
        return future;
    }

    public CompletableFuture<Void> deleteMessage(String queueId, QueueMessage queueMessage) {
        CompletableFuture<Void> future = new CompletableFuture<Void>();
        if (queueMap.containsKey(queueId)) {
            queueMap.get(queueId).deleteMessage(queueMessage);
            future.complete(null);
        } else {
            future.completeExceptionally(new QueueNotFoundException());
        }
        return future;
    }

    public CompletableFuture<Void> removeQueue(String queueId) {
        CompletableFuture<Void> future = new CompletableFuture<Void>();
        if (queueMap.containsKey(queueId)) {
            queueMap.get(queueId).remove();
            future.complete(null);
        } else {
            future.completeExceptionally(new QueueNotFoundException());
        }
        return future;
    }
}