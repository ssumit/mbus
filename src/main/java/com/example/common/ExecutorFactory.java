package com.example.common;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ExecutorFactory {
    private static ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    //todo implement properly, using map and thread pool perhaps
    /**
     * Returns same executor on same key
     * @param key
     * @return
     */
    public ScheduledExecutorService getExecutor(String key) {
        return scheduledExecutorService;
    }
}
