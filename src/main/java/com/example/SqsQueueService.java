package com.example;

import com.amazonaws.services.sqs.AmazonSQSClient;
import com.example.common.QueueConfig;
import com.example.common.QueueMessage;

import java.util.concurrent.CompletableFuture;

public class SqsQueueService implements QueueService {
  //
  // Task 4: Optionally implement parts of me.
  //
  // This file is a placeholder for an AWS-backed implementation of QueueService.  It is included
  // primarily so you can quickly assess your choices for method signatures in QueueService in
  // terms of how well they map to the implementation intended for a production environment.
  //

  public SqsQueueService(AmazonSQSClient sqsClient) {
  }

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
