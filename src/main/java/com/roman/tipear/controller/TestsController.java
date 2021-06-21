package com.roman.tipear.controller;

import com.roman.tipear.model.entity.Texts;
import com.roman.tipear.model.entity.TypingTest;
import com.roman.tipear.model.entity.UserModel;

import java.util.List;
import java.util.Random;
import com.roman.tipear.repository.TextsRepository;
import com.roman.tipear.repository.TypingTestRepository;
import com.roman.tipear.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestsController {

    @Autowired
    TypingTestRepository repo;

    @Autowired
    TextsRepository textsRepo;

    @Autowired
    UserRepository userRepo;

    @GetMapping(value =  "/test/randomText")
    @ResponseBody
    public List<Texts> sendTextInfo() {
        // get a random text entity from db and send to client

        return textsRepo.findAll();
    }

    /*

    // save new test coming from a js req in a json.
    @PostMapping("/test")
    public TypingTest create(@RequestBody String testInfo) {

        // only save if a user is logged in
        // get user or anonymous and compare
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Boolean userLoggedIn = principal instanceof UserDetails;
        if (userLoggedIn) {
            // convert info stored in the string to a new test
            String username = ((UserDetails) principal).getUsername();
            UserModel user = userRepo.findByUsername(username);

            TypingTest test = new TypingTest();
            test.setUser(user);

            return repo.save(test);
        }
       return new TypingTest();
    }
     */

}
