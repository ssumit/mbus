package com.example;

import com.example.common.QueueConfig;
import com.example.common.Time;
import com.example.memQueue.InMemoryQueueService;
import junit.framework.Assert;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class InMemoryQueueTest {

    public static final String MSG_ID = "msg_123";
    public static final String TEST = "test";

    //todo: fix it
    @Test
    public void sendAndReceiveMessage() {
        InMemoryQueueService memoryQueueService = new InMemoryQueueService();
        QueueMessageTestImpl message = new QueueMessageTestImpl(MSG_ID, TEST);
        memoryQueueService.createQueue(new QueueConfig(new Time(30, TimeUnit.SECONDS),
                new Time(1, TimeUnit.DAYS), 1000))
                .thenCompose(id -> {
                    memoryQueueService.sendMessage(id, message)
                            .thenAccept(v -> {
                                memoryQueueService.retrieveMessage(id)
                                        .thenAccept(res -> {
                                            Assert.assertEquals(res.getMessageId(), MSG_ID);
                                            Assert.assertEquals(res.getSerializedForm(), TEST);
                                        });
                            });
                    return null;
                });
    }
}