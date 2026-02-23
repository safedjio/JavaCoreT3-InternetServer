package com.safedjio.multitask.service;

import com.safedjio.multitask.entity.InternetServer;
import com.safedjio.multitask.entity.User;
import com.safedjio.multitask.state.impl.CompletedState;
import com.safedjio.multitask.state.impl.ProcessingState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

public class UserService {
    private static final Logger logger = LogManager.getLogger();

    public void executeServerTask(User user) {
        InternetServer server = InternetServer.getInstance();

        try {
            server.acquireChannel();
            logger.info("User {} connected to a channel.", user.getUserId());

            boolean storageAllocated = false;
            while (!storageAllocated) {
                if (server.reserveSpace(user.getFileSize())) {
                    storageAllocated = true;
                } else {
                    logger.warn("User {} waiting for storage space...", user.getUserId());
                    TimeUnit.MILLISECONDS.sleep(500);
                }
            }

            user.setState(new ProcessingState());
            TimeUnit.SECONDS.sleep(2);

            server.releaseSpace(user.getFileSize());
            user.setState(new CompletedState());

        } catch (InterruptedException e) {
            logger.error("User {} process interrupted!", user.getUserId());
            Thread.currentThread().interrupt();
        } finally {
            server.releaseChannel();
            logger.info("User {} disconnected from channel.", user.getUserId());
        }
    }
}