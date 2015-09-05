package com.example.common;

import java.util.concurrent.TimeUnit;

public class Time {
    private final long magnitude;
    private final TimeUnit timeUnit;

    public Time(long magnitude, TimeUnit timeUnit) {
        this.magnitude = magnitude;
        this.timeUnit = timeUnit;
    }

    public long getMagnitude() {
        return magnitude;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }
}
