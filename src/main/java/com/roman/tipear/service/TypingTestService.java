package com.roman.tipear.service;

import com.roman.tipear.model.entity.TypingTest;

import java.util.Optional;

public interface TypingTestService {
    Optional<TypingTest> findByTestId(Long testId);
}
