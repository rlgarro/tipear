package com.roman.tipear.service;

import com.roman.tipear.model.entity.RegTokenModel;
import com.roman.tipear.model.entity.UserModel;
import com.roman.tipear.model.exception.TokenExpiredException;
import com.roman.tipear.model.exception.UserAlreadyExistsException;
import org.springframework.security.core.userdetails.User;

public interface UserService {
    UserModel findByUsername(String username);
    UserModel findByEmail(String email);
    UserModel register(UserModel user) throws UserAlreadyExistsException;
    void delete(UserModel user);
    void confirmUserByToken(RegTokenModel token) throws TokenExpiredException;
}
