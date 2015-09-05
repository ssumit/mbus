package com.example.common;

public class QueueConfig {
    private final Time visibilityTimeout;
    private final Time messRetentionPeriod;
    private final long maxMessageSize;

    public QueueConfig(Time visibilityTimeout, Time messRetentionPeriod, long maxMessageSize) {
        this.visibilityTimeout = visibilityTimeout;
        this.messRetentionPeriod = messRetentionPeriod;
        this.maxMessageSize = maxMessageSize;
    }

    public Time getVisibilityTimeout() {
        return visibilityTimeout;
    }

    public Time getMessRetentionPeriod() {
        return messRetentionPeriod;
    }

    public long getMaxMessageSize() {
        return maxMessageSize;
    }
}
