package com.roman.tipear.repository;

import com.roman.tipear.model.entity.TypingTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypingTestRepository extends JpaRepository<TypingTest, Long> {
}
