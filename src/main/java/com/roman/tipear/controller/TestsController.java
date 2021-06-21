package com.roman.tipear.controller;

import com.roman.tipear.model.entity.Texts;
import com.roman.tipear.model.entity.TypingTest;
import com.roman.tipear.model.entity.UserModel;

import java.util.*;

import com.roman.tipear.repository.TextsRepository;
import com.roman.tipear.repository.TypingTestRepository;
import com.roman.tipear.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class TestsController {

    @Autowired
    TypingTestRepository repo;

    @Autowired
    TextsRepository textsRepo;

    @Autowired
    UserRepository userRepo;

    @GetMapping(value =  "/test/text")
    @ResponseBody
    public List<Texts> sendTextInfo() {
        return textsRepo.findAll();
    }

    // save new test coming from a js req in a json.
    @PostMapping(value = "/test")
    public String createTest(@ModelAttribute TypingTest test) {

        // only save if a user is logged in
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Boolean userLoggedIn = principal instanceof UserDetails;
        if (userLoggedIn) {
            String username = ((UserDetails) principal).getUsername();
            UserModel user = userRepo.findByUsername(username);

            test.setAuthor(test.getAuthor());
            test.setTextId(textsRepo.findByTitle(test.getTitle()).getId());
            test.setUser(user);

            repo.save(test);
            return "redirect:/u/".concat(user.getUsername());
        }
       return "index";
    }

}
