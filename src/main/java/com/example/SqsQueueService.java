package com.example;

import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.example.common.QueueConfig;
import com.example.common.QueueMessage;

import java.util.concurrent.CompletableFuture;

public class SqsQueueService implements QueueService {
    private AmazonSQSClient sqsClient;
    //
    // Task 4: Optionally implement parts of me.
    //
    // This file is a placeholder for an AWS-backed implementation of QueueService.  It is included
    // primarily so you can quickly assess your choices for method signatures in QueueService in
    // terms of how well they map to the implementation intended for a production environment.
    //

    public SqsQueueService(AmazonSQSClient sqsClient) {
        this.sqsClient = sqsClient;
    }

    public CompletableFuture<String> createQueue(QueueConfig queueConfig) {
        try {
            CreateQueueResult queue = sqsClient.createQueue(new CreateQueueRequest());
            return CompletableFuture.completedFuture(queue.getQueueUrl());
        } catch (Exception e) {
            return getExceptionFuture(e);
        }
    }

    public CompletableFuture<Void> sendMessage(String queueId, QueueMessage queueMessage) {
        try {
            sqsClient.sendMessage(queueId, queueMessage.getSerializedForm());
            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            return getExceptionFuture(e);
        }
    }

    public CompletableFuture<QueueMessage> retrieveMessage(String queueId) {
        try {
            ReceiveMessageResult result = sqsClient.receiveMessage(queueId);
            if (result.getMessages().size() > 0) {
                return CompletableFuture.completedFuture(new QueueMessage() {
                    @Override
                    public String getMessageId() {
                        return result.getMessages().get(0).getReceiptHandle();
                    }

                    @Override
                    public String getSerializedForm() {
                        return result.getMessages().get(0).getBody();
                    }
                });
            } else {
                return CompletableFuture.completedFuture(null);
            }
        } catch (Exception e) {
            return getExceptionFuture(e);
        }
    }

    public CompletableFuture<Void> deleteMessage(String queueId, QueueMessage queueMessage) {
        try {
            sqsClient.deleteMessage(queueId, queueMessage.getMessageId());
            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            return getExceptionFuture(e);
        }
    }

    public CompletableFuture<Void> removeQueue(String queueId) {
        try {
            sqsClient.deleteQueue(queueId);
            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            return getExceptionFuture(e);
        }
    }

    private <T> CompletableFuture<T> getExceptionFuture(Exception e) {
        CompletableFuture<T> future = new CompletableFuture<>();
        future.completeExceptionally(e);
        return future;
    }
}