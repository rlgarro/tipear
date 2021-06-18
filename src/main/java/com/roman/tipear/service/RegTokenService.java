package com.roman.tipear.service;

import com.roman.tipear.model.entity.RegTokenModel;
import com.roman.tipear.model.entity.UserModel;

public interface RegTokenService {
    RegTokenModel register(UserModel user);
    RegTokenModel findByToken(String token);
}
