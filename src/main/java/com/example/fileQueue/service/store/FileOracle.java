package com.example.fileQueue.service.store;

import com.example.common.QueueConfig;
import com.example.common.QueueMessage;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

//should be extracted into interface
public class FileOracle {

    //responsible for cleaning up of records after renntion period expiration

    public CompletableFuture<Boolean> createNewFile(String queueName, QueueConfig queueConfig) {
        //new table for queueName and new entry in file table
        return null;
    }

    public CompletableFuture<Boolean> getFileLock(String queueName) {
        //check and get from config table
        return null;
    }

    public CompletableFuture<Void> write(String queueName, QueueMessage queueMessage) {
        //will wait on getting file lock.
        //change in file table
        return null;
    }

    public CompletableFuture<Boolean> setFileLockFree(String queueName) {
        //change config table
        return null;
    }

    public CompletableFuture<Boolean> getRecordFromFile(String queueName) {
        //change in file table
        return null;
    }

    public CompletableFuture<Boolean> deleteRecord(String queueName, QueueMessage queueMessage) {
        return null;
    }

    public CompletableFuture<Void> deleteFile(String queueId) {
        return null;
    }
}