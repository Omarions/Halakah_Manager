package com.omarionapps.halaka.service;

import com.omarionapps.halaka.model.User;

/**
 * Created by Omar on 22-Apr-17.
 */
public interface UserService {

    User findUserByEmail(String email);

    void saveUser(User user);

    User findUserByUserDetails();
}
