package com.example.memQueue;

import com.example.QueueAlreadyFullException;
import com.example.common.QueueConfig;
import com.example.common.Time;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Removes only invisible items and makes items invisible for x seconds after every peek operation.
 * Milliseconds is assumed to be smallest time unit for calculations purposes.
 *
 * @param <T>
 */
public class QueueConfigQueue<T> {

    private final List<Element<T>> list;
    private final List<Element<T>> invisibleItems;
    private final ScheduledExecutorService scheduledExecutorService;
    private QueueConfig queueConfig;

    public QueueConfigQueue(QueueConfig queueConfig) {
        this.queueConfig = queueConfig;
        list = new ArrayList<>();
        invisibleItems = new ArrayList<>();
        scheduleToMakeItemsVisible(queueConfig.getVisibilityTimeout());
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    }

    public long size() {
        return list.size() + invisibleItems.size();
    }

    public void add(T t) {
        if (size() > queueConfig.getMaxMessageSize()) {
            throw new QueueAlreadyFullException();
        }
        Element<T> e = new Element<>(t, System.currentTimeMillis());
        list.add(e);
        scheduleRemoval(e);
    }

    private void scheduleRemoval(Element<T> e) {
        scheduledExecutorService.schedule(() -> {
            list.remove(e);
            invisibleItems.remove(e);
        }, queueConfig.getMessRetentionPeriod().getTimeUnit().toMillis(
                queueConfig.getMessRetentionPeriod().getMagnitude()), TimeUnit.MILLISECONDS);
    }

    public T peek() {
        if (list.size() == 0) {
            return null;
        } else {
            Element<T> first = list.remove(0);
            invisibleItems.add(first);
            return first.getT();
        }
    }

    public void remove(T t) {
        invisibleItems.remove(new Element<T>(t, System.currentTimeMillis()));//todo : fix it, not a good way,
    }

    private void scheduleToMakeItemsVisible(final Time visibilityTimeout) {
        scheduledExecutorService.schedule(new Runnable() {
            public void run() {
                List<Element<T>> copy = new ArrayList<Element<T>>();
                copy.addAll(invisibleItems);
                for (Element<T> invisibleItem : copy) {
                    long currTime = System.currentTimeMillis();
                    if (currTime - invisibleItem.getTimeInMillis() > visibilityTimeout.getTimeUnit().toMillis(visibilityTimeout.getMagnitude())) {
                        list.add(0, invisibleItem);
                    }
                }

            }
        }, visibilityTimeout.getMagnitude(), visibilityTimeout.getTimeUnit());
    }

    static class Element<T> {
        final T t;
        final long timeInMillis;

        Element(T t, long timeInMillis) {
            this.t = t;
            this.timeInMillis = timeInMillis;
        }

        public T getT() {
            return t;
        }

        public long getTimeInMillis() {
            return timeInMillis;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Element<?> element = (Element<?>) o;

            return !(t != null ? !t.equals(element.t) : element.t != null);

        }

        @Override
        public int hashCode() {
            return t != null ? t.hashCode() : 0;
        }
    }
}