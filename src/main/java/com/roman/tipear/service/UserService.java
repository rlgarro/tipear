package com.roman.tipear.service;

import com.roman.tipear.model.entity.UserModel;
import com.roman.tipear.model.exception.UserAlreadyExistsException;

public interface UserService {
    UserModel findByUsername(String username);
    UserModel findByEmail(String email);
    UserModel register(UserModel user) throws UserAlreadyExistsException;
}
