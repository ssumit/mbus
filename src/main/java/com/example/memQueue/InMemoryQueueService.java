package com.example.memQueue;

import com.example.QueueNotFoundException;
import com.example.common.QueueConfig;
import com.example.common.QueueMessage;
import com.example.QueueService;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Use thread confinement as synchronized keyword was getting messy.
 */
public class InMemoryQueueService implements QueueService {
    private final Map<String, MemoryQueue> queueMap;
    private final ExecutorFactory executorFactory;

    public InMemoryQueueService() {
        queueMap = new ConcurrentHashMap<>();
        executorFactory = new ExecutorFactory();
    }

    public CompletableFuture<String> createQueue(QueueConfig queueConfig) {
        MemoryQueue memoryQueue = new MemoryQueue(queueConfig);
        String uniqueId = UUID.randomUUID().toString();
        queueMap.put(uniqueId, memoryQueue);
        return CompletableFuture.completedFuture(uniqueId);
    }

    public CompletableFuture<Void> sendMessage(final String queueId, final QueueMessage queueMessage) {
        final CompletableFuture<Void> future = new CompletableFuture<Void>();
        scheduleNow(() -> {
            if (queueMap.containsKey(queueId)) {
                try {
                    queueMap.get(queueId).addMessage(queueMessage);
                } catch (Exception e) {
                    future.completeExceptionally(e);
                }
            } else {
                future.completeExceptionally(new QueueNotFoundException());
            }
        }, queueId);
        return future;
    }

    public CompletableFuture<QueueMessage> retrieveMessage(String queueId) {
        CompletableFuture<QueueMessage> future = new CompletableFuture<>();
        scheduleNow(() -> {
            if (queueMap.containsKey(queueId)) {
                future.complete(queueMap.get(queueId).getMessage());
            } else {
                future.completeExceptionally(new QueueNotFoundException());
            }
        }, queueId);
        return future;
    }

    public CompletableFuture<Void> deleteMessage(String queueId, QueueMessage queueMessage) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        scheduleNow(() -> {
            if (queueMap.containsKey(queueId)) {
                queueMap.get(queueId).deleteMessage(queueMessage);
                future.complete(null);
            } else {
                future.completeExceptionally(new QueueNotFoundException());
            }
        }, queueId);
        return future;
    }

    public CompletableFuture<Void> removeQueue(String queueId) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        scheduleNow(() -> {
            if (queueMap.containsKey(queueId)) {
                queueMap.get(queueId).remove();
                future.complete(null);
            } else {
                future.completeExceptionally(new QueueNotFoundException());
            }
        }, queueId);
        return future;
    }

    private void scheduleNow(Runnable runnable, String queueId) {
        executorFactory.getExecutor(queueId).schedule(runnable, 0, TimeUnit.MILLISECONDS);
    }
}