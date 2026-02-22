package com.safedjio.multitask.entity;

import com.safedjio.multitask.exception.ServerException;
import com.safedjio.multitask.service.UserService;
import com.safedjio.multitask.state.UserState;
import com.safedjio.multitask.state.impl.WaitingState;
import java.util.concurrent.Callable;

public class User implements Callable<String> {
    private final int userId;
    private final int fileSize;
    private UserState state;

    public User(int userId, int fileSize) {
        this.userId = userId;
        this.fileSize = fileSize;
        this.state = new WaitingState();
    }

    public int getUserId() { return userId; }
    public int getFileSize() { return fileSize; }

    public void setState(UserState state) {
        this.state = state;
        this.state.handle(this);
    }

    @Override
    public String call() throws ServerException {
        UserService action = new UserService();
        action.executeServerTask(this);
        return "User " + userId + " processed";
    }
}