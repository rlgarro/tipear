package com.roman.tipear.implementation;

import com.roman.tipear.model.entity.TokenModel;
import com.roman.tipear.model.entity.UserModel;
import com.roman.tipear.repository.TokenRepository;
import com.roman.tipear.repository.UserRepository;
import com.roman.tipear.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public TokenModel findByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    @Override
    public boolean tokenByEmail(String email) {
        return userRepository.findByEmail(email).getToken() == null;
    }

    @Override
    public TokenModel register(UserModel user) {
        String tokenStr = UUID.randomUUID().toString();
        return tokenRepository.save(new TokenModel(tokenStr, user));
    }
}
