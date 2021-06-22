package com.roman.tipear.repository;

import com.roman.tipear.model.entity.TypingTest;
import javassist.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypingTestRepository extends JpaRepository<TypingTest, Long> {
    @Query(value = "SELECT * FROM user_tests WHERE user_id = ?1 ORDER BY test_id DESC", nativeQuery = true)
    List<TypingTest> findAllByUserId(Long id) throws NotFoundException;
}