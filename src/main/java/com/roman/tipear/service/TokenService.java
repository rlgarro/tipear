package com.roman.tipear.service;

import com.roman.tipear.model.entity.TokenModel;
import com.roman.tipear.model.entity.UserModel;

public interface TokenService {
    TokenModel register(UserModel user);
    TokenModel findByToken(String token);
    boolean tokenByEmail(String username);
}
