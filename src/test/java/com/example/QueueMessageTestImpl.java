package com.example;

import com.example.common.QueueMessage;

public class QueueMessageTestImpl implements QueueMessage {
    private final String id;
    private final String message;

    public QueueMessageTestImpl(String id, String message) {
        this.id = id;
        this.message = message;
    }

    @Override
    public String getMessageId() {
        return id;
    }

    @Override
    public String getSerializedForm() {
        return message;
    }
}
