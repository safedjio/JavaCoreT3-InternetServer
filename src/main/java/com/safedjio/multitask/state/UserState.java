package com.safedjio.multitask.state;

import com.safedjio.multitask.entity.User;

public interface UserState {
    void handle(User user);
}