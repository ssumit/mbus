package com.example.memQueue;

import com.example.QueueAlreadyFullException;
import com.example.QueueAlreadyRequestedTobeDeletedException;
import com.example.common.QueueConfig;
import com.example.common.QueueMessage;

import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public class MemoryQueue {
    private final QueueConfig queueConfig;
    private final VisibilityTimeOutQueue<QueueMessage> queueMessageList;
    private boolean isQueueMarkedToBeDeleted = false;

    public MemoryQueue(QueueConfig queueConfig) {
        this.queueConfig = queueConfig;
        queueMessageList = new VisibilityTimeOutQueue<QueueMessage>(queueConfig.getVisibilityTimeout());
    }

    public void addMessage(QueueMessage queueMessage) {
        if (isQueueMarkedToBeDeleted) {
            throw new QueueAlreadyRequestedTobeDeletedException();
        } else if (queueMessageList.size() > queueConfig.getMaxMessageSize()) {
            throw new QueueAlreadyFullException();
        } else {
            queueMessageList.add(queueMessage);
        }
    }

    public QueueMessage getMessage() {
        if (queueMessageList.size() > 0) {
            return queueMessageList.peek();
        } else {
            return null;
        }
    }

    public void deleteMessage(QueueMessage queueMessage) {
        queueMessageList.remove(queueMessage);
    }

    public void remove() {
        isQueueMarkedToBeDeleted = true;
    }
}