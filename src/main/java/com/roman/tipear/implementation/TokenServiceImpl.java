package com.roman.tipear.implementation;

import com.roman.tipear.model.entity.RegTokenModel;
import com.roman.tipear.model.entity.UserModel;
import com.roman.tipear.repository.RegTokenRepository;
import com.roman.tipear.service.RegTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TokenServiceImpl implements RegTokenService {

    @Autowired
    RegTokenRepository tokenRepository;

    @Override
    public RegTokenModel findByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    @Override
    public RegTokenModel register(UserModel user) {
        String tokenStr = UUID.randomUUID().toString();
        return tokenRepository.save(new RegTokenModel(tokenStr, user));
    }
}
