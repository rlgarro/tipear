package com.roman.tipear.controller;

import com.roman.tipear.implementation.UserServiceImp;
import com.roman.tipear.model.entity.UserModel;
import com.roman.tipear.model.exception.UserAlreadyExistsException;
import com.roman.tipear.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.nio.file.Path;
import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private UserService userDetailsService;

    @Autowired
    private UserServiceImp userServiceImp;

    @GetMapping("/u/{username}")
    public String userProfile(Authentication auth, HttpSession session, @PathVariable String username, Model model) {

        // check that the username on the variable exists
        Boolean userExists = userDetailsService.findByUsername(username) != null;
        if (!userExists) {
            return "404";
        }

        // check if user session is the same as user in path variable
        UserModel userRequested = userDetailsService.findByUsername(username);
        session.setAttribute("userRequested", userRequested);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userInSession = "";

        if (principal instanceof UserDetails) {
           userInSession = ((UserDetails) principal).getUsername();
           Boolean userIsTheSame = userInSession.equals(userRequested.getUsername());
           if (userIsTheSame) {
               session.setAttribute("userAuthorized", true);
           } else {
               session.setAttribute("userAuthorized", false);
           }
        } else {
            session.setAttribute("userAuthorized", false);
        }

        return "user";
    }

    @GetMapping("/register")
    public String registrationForm(Model model) {
       model.addAttribute("user", new UserModel());
       return "register";
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("user", new UserModel());
        return "login";
    }

    @GetMapping("/logout")
    public String logout(Authentication auth, HttpSession session) {
        session.invalidate();
        return "index";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute UserModel user, Model model) {
        try {
            model.addAttribute("user", userDetailsService.register(user));
        } catch (UserAlreadyExistsException e) {
            model.addAttribute("error ".concat(String.valueOf(e)));
            return "redirect:/register?error=true";
        }
        return "redirect:/login";
    }
}