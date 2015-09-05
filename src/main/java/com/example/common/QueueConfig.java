package com.example.common;

public class QueueConfig {
    private Time visibilityPeriod;
    private Time messRetentionPeriod;
    private long maxMessageSize;

    public Time getVisibilityPeriod() {
        return visibilityPeriod;
    }

    public void setVisibilityPeriod(Time visibilityPeriod) {
        this.visibilityPeriod = visibilityPeriod;
    }

    public Time getMessRetentionPeriod() {
        return messRetentionPeriod;
    }

    public void setMessRetentionPeriod(Time messRetentionPeriod) {
        this.messRetentionPeriod = messRetentionPeriod;
    }

    public long getMaxMessageSize() {
        return maxMessageSize;
    }

    public void setMaxMessageSize(long maxMessageSize) {
        this.maxMessageSize = maxMessageSize;
    }
}
