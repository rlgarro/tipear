package com.roman.tipear.controller;

import com.roman.tipear.model.entity.Texts;
import com.roman.tipear.repository.TextsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class TextsController {

    @Autowired
    TextsRepository repo;

    @GetMapping("/texts/all")
    public List<Texts> getAll() {
        return repo.findAll();
    }

    @GetMapping("/texts/{textId}")
    public Optional<Texts> getSpecificText(@PathVariable Long textId) {

        return repo.findById(textId);
    }

}
