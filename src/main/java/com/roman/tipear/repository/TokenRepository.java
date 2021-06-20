package com.roman.tipear.repository;

import com.roman.tipear.model.entity.TokenModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<TokenModel, Long> {
    TokenModel findByToken(String token);
}
