package com.roman.tipear.implementation;

import com.roman.tipear.model.entity.TypingTest;
import com.roman.tipear.repository.TypingTestRepository;
import com.roman.tipear.service.TypingTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TestServiceImpl implements TypingTestService {

    @Autowired
    TypingTestRepository repo;

    public Optional<TypingTest> findByTestId(Long testId){
        return repo.findById(testId);
    }
}