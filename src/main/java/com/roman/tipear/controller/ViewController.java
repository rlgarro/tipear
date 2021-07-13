package com.roman.tipear.controller;

import com.roman.tipear.model.entity.UserModel;
import com.roman.tipear.repository.TextsRepository;
import com.roman.tipear.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class ViewController {

    @Autowired
    TextsRepository textsRepo;

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

    @GetMapping("/test/{time}")
    public String test(@PathVariable int time, Authentication auth, HttpSession session, Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            UserModel user = userService.findByUsername(((UserDetails) principal).getUsername());
            session.setAttribute("user", user);
        }
        model.addAttribute("time", time);
        return "test";
    }

    @GetMapping("/race/{roomId}")
    public String race(@PathVariable String roomId, Model model){
        model.addAttribute("roomId", roomId);
        return "race";
    }

    @GetMapping("/race")
    public String race() {
        return "race";
    }

}