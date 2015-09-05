package com.example.memQueue;

import com.example.QueueAlreadyFullException;
import com.example.QueueAlreadyRequestedTobeDeletedException;
import com.example.common.QueueConfig;
import com.example.common.QueueMessage;

import javax.annotation.concurrent.ThreadSafe;
import java.util.ArrayList;
import java.util.List;

@ThreadSafe
public class MemoryQueue {
    private QueueConfig queueConfig;
    private final List<QueueMessage> queueMessageList;
    private volatile boolean isQueueMarkedToBeDeleted = false;

    public MemoryQueue(QueueConfig queueConfig) {
        this.queueConfig = queueConfig;
        queueMessageList = new ArrayList<QueueMessage>();
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
            return queueMessageList.get(0);
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