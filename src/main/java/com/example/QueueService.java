package com.example;

import com.example.common.QueueConfig;
import com.example.common.QueueMessage;

import java.util.concurrent.CompletableFuture;

public interface QueueService {

    /**
     * Creates a new queue.
     *
     * @param queueConfig - contains all the params that needs to be over-written.
     * @return Unique queue id.
     */
    public CompletableFuture<String> createQueue(QueueConfig queueConfig);

    /**
     * sends a single message to a particular queue.
     * @param queueId unique queue id.
     * @param queueMessage queue message to be sent
     * @return
     */
    public CompletableFuture<Void> sendMessage(String queueId, QueueMessage queueMessage);

    /**
     * retrives a single message from a particular queue.
     * @param queueId unique queue id.
     * @return retrieved queue message.
     */
    public CompletableFuture<QueueMessage> retrieveMessage(String queueId);

    /**
     * deletes a single message from a particular queue.
     * @param queueId unique queue id.
     * @param queueMessage queue message to be sent
     * @return
     */
    public CompletableFuture<Void> deleteMessage(String queueId, QueueMessage queueMessage);

    /**
     * Removes the queue.
     *
     * @param queueId unique queue id.
     * @return.
     */
    public CompletableFuture<Void> removeQueue(String queueId);
}