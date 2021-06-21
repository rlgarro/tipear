package com.roman.tipear.repository;

import com.roman.tipear.model.entity.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.w3c.dom.stylesheets.LinkStyle;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
    UserModel findByUsername(String username);
    UserModel findByEmail(String email);

    @Modifying
    @Transactional
    @Query(value = "UPDATE UserModel u SET u.username = ?2, u.email = ?3, u.password = ?4 WHERE u.id = ?1")
    void update(Long id, String username, String email, String password);

}
