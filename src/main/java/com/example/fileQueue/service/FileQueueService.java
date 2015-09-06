package com.example.fileQueue.service;

import com.example.QueueNotFoundException;
import com.example.QueueService;
import com.example.common.ExecutorFactory;
import com.example.common.QueueConfig;
import com.example.common.QueueMessage;
import com.example.fileQueue.service.store.FileOracle;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class FileQueueService implements QueueService {
    private final List<String> queueList;
    private final ExecutorFactory executorFactory;
    private final FileOracle fileOracle;

    public FileQueueService() {
        queueList = new ArrayList<>();
        executorFactory = new ExecutorFactory();
        fileOracle = new FileOracle();
    }

    //queueId will act as queueName here and can be injected as method param
    public CompletableFuture<String> createQueue(QueueConfig queueConfig) {
        String uniqueId = UUID.randomUUID().toString();
        queueList.add(uniqueId);
        fileOracle.createNewFile(uniqueId, queueConfig);
        return CompletableFuture.completedFuture(uniqueId);
    }

    public CompletableFuture<Void> sendMessage(final String queueId, final QueueMessage queueMessage) {
        final CompletableFuture<Void> future = new CompletableFuture<>();
        scheduleNow(() -> {
            if (queueList.contains(queueId)) {
                fileOracle.getFileLock(queueId)
                        .thenCompose(v -> fileOracle.write(queueId, queueMessage))
                        .thenCompose(v -> fileOracle.setFileLockFree(queueId))
                        .whenComplete((a, t) -> {
                            if (t != null) {
                                future.complete(null);
                            } else {
                                future.completeExceptionally(t);
                            }
                        });
            } else {
                future.completeExceptionally(new QueueNotFoundException());
            }
        }, queueId);
        return future;
    }

    public CompletableFuture<QueueMessage> retrieveMessage(String queueId) {
        CompletableFuture<QueueMessage> future = new CompletableFuture<>();
        scheduleNow(() -> {
            if (queueList.contains(queueId)) {
                fileOracle.getFileLock(queueId)
                        .thenCompose(v -> fileOracle.getRecordFromFile(queueId))
                        .thenCompose(v -> fileOracle.setFileLockFree(queueId))
                        .whenComplete((a, t) -> {
                            if (t != null) {
                                future.complete(null);
                            } else {
                                future.completeExceptionally(t);
                            }
                        });
            } else {
                future.completeExceptionally(new QueueNotFoundException());
            }
        }, queueId);
        return future;
    }

    public CompletableFuture<Void> deleteMessage(String queueId, QueueMessage queueMessage) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        scheduleNow(() -> {
            if (queueList.contains(queueId)) {
                fileOracle.getFileLock(queueId)
                        .thenCompose(v -> fileOracle.deleteRecord(queueId, queueMessage))
                        .thenCompose(v -> fileOracle.setFileLockFree(queueId))
                        .whenComplete((a, t) -> {
                            if (t != null) {
                                future.complete(null);
                            } else {
                                future.completeExceptionally(t);
                            }
                        });
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
            if (queueList.contains(queueId)) {
                fileOracle.getFileLock(queueId)
                        .thenCompose(v -> fileOracle.deleteFile(queueId))
                        .whenComplete((a, t) -> {
                            if (t != null) {
                                future.complete(null);
                            } else {
                                future.completeExceptionally(t);
                            }
                        });
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