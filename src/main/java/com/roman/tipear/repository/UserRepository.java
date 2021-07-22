package com.roman.tipear.repository;

import com.roman.tipear.model.entity.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
    UserModel findByUsername(String username);
    UserModel findByEmail(String email);

    @Modifying
    @Transactional
    @Query(value = "UPDATE UserModel u SET u.username = ?2, u.email = ?3, u.password = ?4 WHERE u.id = ?1")
    void fullUpdate(Long id, String username, String email, String password);

    @Modifying
    @Transactional
    @Query(value = "UPDATE UserModel u SET u.username = ?2, u.email = ?3 WHERE u.id = ?1")
    void parcialUpdate(Long id, String username, String email);
}
