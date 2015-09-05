package com.example.memQueue;

import com.example.common.Time;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Removes only invisible items and makes items invisible for x seconds after every peek operation.
 *
 * @param <T>
 */
public class VisibilityTimeOutQueue<T> {

    private final List<T> list;
    private final List<Element<T>> invisibleItems;

    public VisibilityTimeOutQueue(Time visibilityTimeout) {
        list = new ArrayList<T>();
        invisibleItems = new ArrayList<Element<T>>();
        scheduleToMakeItemsVisible(visibilityTimeout);
    }

    public long size() {
        return list.size() + invisibleItems.size();
    }

    public void add(T t) {
        list.add(t);
    }

    public T peek() {
        T first = list.remove(0);
        invisibleItems.add(new Element<T>(first, System.currentTimeMillis()));
        return first;
    }

    public void remove(T t) {
        invisibleItems.remove(new Element<T>(t, System.currentTimeMillis()));//todo : fix it, not a good way,
    }

    private void scheduleToMakeItemsVisible(final Time visibilityTimeout) {
        Executors.newSingleThreadScheduledExecutor().schedule(new Runnable() {
            public void run() {
                List<Element<T>> copy = new ArrayList<Element<T>>();
                copy.addAll(invisibleItems);
                for (Element<T> invisibleItem : copy) {
                    long currTime = System.currentTimeMillis();
                    if (currTime - invisibleItem.getTimeInMillis() > visibilityTimeout.getTimeUnit().toMillis(visibilityTimeout.getMagnitude())) {
                        list.add(0, invisibleItem.getT());
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