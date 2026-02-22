package com.safedjio.multitask.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class InternetServer {
    private static final Logger logger = LogManager.getLogger();

    private final int maxStorageSize = 1000;
    private int currentStorageSize = 0;

    private final Semaphore channelSemaphore = new Semaphore(3, true);
    private final ReentrantLock storageLock = new ReentrantLock();

    private InternetServer() {}

    private static class ServerHolder {
        private static final InternetServer INSTANCE = new InternetServer();
    }

    public static InternetServer getInstance() {
        return ServerHolder.INSTANCE;
    }

    public void acquireChannel() throws InterruptedException {
        channelSemaphore.acquire();
    }

    public void releaseChannel() {
        channelSemaphore.release();
    }

    public boolean reserveSpace(int size) {
        storageLock.lock();
        try {
            if (currentStorageSize + size <= maxStorageSize) {
                currentStorageSize += size;
                logger.info("Server: Reserved {} MB. Current usage: {}/{}", size, currentStorageSize, maxStorageSize);
                return true;
            }
            return false;
        } finally {
            storageLock.unlock();
        }
    }

    public void releaseSpace(int size) {
        storageLock.lock();
        try {
            currentStorageSize -= size;
            logger.info("Server: Released {} MB. Current usage: {}/{}", size, currentStorageSize, maxStorageSize);
        } finally {
            storageLock.unlock();
        }
    }
}