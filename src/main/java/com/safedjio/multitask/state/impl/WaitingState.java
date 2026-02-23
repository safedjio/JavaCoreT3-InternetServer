package com.safedjio.multitask.state.impl;

import com.safedjio.multitask.entity.User;
import com.safedjio.multitask.state.UserState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WaitingState implements UserState {
    private static final Logger logger = LogManager.getLogger();
    @Override
    public void handle(User user) {
        logger.info("User {} is in WAITING state (waiting for channel/storage).", user.getUserId());
    }
}