package com.omarionapps.halaka.service;

import com.omarionapps.halaka.model.User;

/**
 * Created by Omar on 22-Apr-17.
 */
public interface UserService {
    public User findUserByEmail(String email);
    public void saveUser(User user);
}
