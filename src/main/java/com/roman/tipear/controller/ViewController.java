package com.roman.tipear.controller;

import com.roman.tipear.model.entity.UserModel;
import com.roman.tipear.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class ViewController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index(Authentication auth, HttpSession session) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            UserModel user = userService.findByUsername(((UserDetails) principal).getUsername());
            session.setAttribute("user", user);
        }
        return "index";
    }
    @GetMapping("/test")
    public String testPage(Authentication auth, HttpSession session) {
        if(session.getAttribute("user") != null) {
            UserModel user = userService.findByUsername(auth.getName());
            session.setAttribute("user", user);
        }
        return "test";
    }
}