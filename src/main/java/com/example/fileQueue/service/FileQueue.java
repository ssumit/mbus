package com.example.fileQueue.service;

import com.example.common.QueueConfig;
import com.example.common.QueueMessage;
import com.google.common.io.Files;

import java.io.*;
import java.nio.charset.Charset;

public class FileQueue {
    public static final String UTF_8 = "UTF-8";
    private static final String COLON = ":";
    private final QueueConfig queueConfig;
    private boolean isQueueMarkedToBeDeleted = false;
    private final File file;

    public FileQueue(QueueConfig queueConfig, String fileName) {
        this.queueConfig = queueConfig;
        this.file = new File(fileName);
    }

    public void addMessage(QueueMessage queueMessage) throws IOException {
        if (!isQueueMarkedToBeDeleted) {
            writeFile(queueMessage.getMessageId() + COLON + queueMessage.getSerializedForm());
        }
    }

    public QueueMessage getMessage() {
        return null;
    }

    public void deleteMessage(QueueMessage queueMessage) {
    }

    public void remove() {
    }

    public void insertConfig() throws IOException {
        writeFile("");
    }

    static int writeAttemptCount = 0;

    public boolean writeFile(String str) throws IOException {
        try {
            Files.write(str, file, Charset.defaultCharset());
            writeAttemptCount = 0;
            return true;
        } catch (IOException err) // Can't access
        {
            writeAttemptCount++;
            try {
                Thread.sleep(200);
                if (writeAttemptCount < 2000) {
                    return writeFile(str);
                } else {
                    writeAttemptCount = 0;
                    throw err;
                }
            } catch (InterruptedException err2) {
                return writeFile(str);
            }
        }
    }
}