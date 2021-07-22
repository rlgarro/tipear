package com.roman.tipear.service;

import com.roman.tipear.model.entity.TokenModel;
import com.roman.tipear.model.entity.UserModel;
import com.roman.tipear.model.exception.UserAlreadyExistsException;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;

public interface UserService {
    UserModel findByUsername(String username);
    UserModel findByEmail(String email);
    UserModel register(UserModel user) throws UserAlreadyExistsException;
    void delete();
    void confirmUserByToken(TokenModel token);
    Model setUserProfileAuthBased(Model model, HttpSession session, String username);
    void updateUserData(UserModel user, Long userId);
    String createRecoverToken(UserModel user);
    void passwordChange(UserModel newPassword, HttpSession session);
}
