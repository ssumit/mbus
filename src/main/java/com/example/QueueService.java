package com.example;

import java.util.concurrent.CompletableFuture;

public interface QueueService {

    /**
     * Creates a new queue.
     * @param queueConfig - contains all the params that needs to be over-written.
     * @return Unique queue id.
     */
    public CompletableFuture<String> createQueue(QueueConfig queueConfig);

    public CompletableFuture<Void> sendMessage(String queueId, QueueMessage queueMessage);

    public CompletableFuture<QueueMessage> retrieveMessage(String queueId);

    public CompletableFuture<Void> deleteMessage(String queueId, String messageId);

    public CompletableFuture<String> removeQueue(String queueId);
  //
  // Task 1: Define me.
  //
  // This interface should include the following methods.  You should choose appropriate
  // signatures for these methods that prioritise simplicity of implementation for the range of
  // intended implementations (in-memory, file, and SQS).  You may include additional methods if
  // you choose.
  //
  // - push
  //   pushes a message onto a queue.
  // - pull
  //   retrieves a single message from a queue.
  // - delete
  //   deletes a message from the queue that was received by pull().
  //

}
