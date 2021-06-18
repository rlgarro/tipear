package com.roman.tipear.repository;

import com.roman.tipear.model.entity.RegTokenModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RegTokenRepository extends JpaRepository<RegTokenModel, Long> {
    RegTokenModel findByToken(String token);
}
