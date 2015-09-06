package com.example.memQueue;

import com.example.QueueAlreadyRequestedTobeDeletedException;
import com.example.common.QueueConfig;
import com.example.common.QueueMessage;

import javax.annotation.concurrent.NotThreadSafe;

//this class is very slim and can be removed
@NotThreadSafe
public class MemoryQueue {
    private final QueueConfigQueue<QueueMessage> queueMessageList;
    private boolean isQueueMarkedToBeDeleted = false;

    public MemoryQueue(QueueConfig queueConfig) {
        queueMessageList = new QueueConfigQueue<>(queueConfig);
    }

    public void addMessage(QueueMessage queueMessage) {
        if (isQueueMarkedToBeDeleted) {
            throw new QueueAlreadyRequestedTobeDeletedException();
        } else {
            queueMessageList.add(queueMessage);
        }
    }

    public QueueMessage getMessage() {
        return queueMessageList.peek();
    }

    public void deleteMessage(QueueMessage queueMessage) {
        queueMessageList.remove(queueMessage);
    }

    public void remove() {
        isQueueMarkedToBeDeleted = true;
    }
}