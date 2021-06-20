package com.roman.tipear.controller;

import com.roman.tipear.email.EmailSenderService;
import com.roman.tipear.implementation.UserServiceImpl;
import com.roman.tipear.model.entity.TokenModel;
import com.roman.tipear.model.entity.UserModel;
import com.roman.tipear.model.exception.TokenExpiredException;
import com.roman.tipear.model.exception.UserAlreadyExistsException;
import com.roman.tipear.repository.UserRepository;
import com.roman.tipear.service.TokenService;
import com.roman.tipear.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {

    @Autowired
    private UserService userDetailsService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository repository;

    @Autowired
    EmailSenderService emailSender;


    // get mappings
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
           userInSession = userServiceImpl.getUsernameFromPrincipal(principal);
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


    @GetMapping("/confirm/{token}")
    public String confirmUser(Model model, @PathVariable String token) throws TokenExpiredException {
        TokenModel tokenEntity = tokenService.findByToken(token);

        try {
          userDetailsService.confirmUserByToken(tokenEntity);
        } catch (TokenExpiredException tnf) {
           model.addAttribute("error", "Couldn't confirm your account");
            return "error";
        }

        return "redirect:/login";

    }

    @GetMapping("/recover/{token}")
    public String recoverPassword(Model model, @PathVariable String token) throws TokenExpiredException {
        TokenModel tokenEntity = tokenService.findByToken(token);

       return "recover?confirmed=true";
    }

    // post mappings
    @PostMapping("/recover")
    public String createRecoverToken(@ModelAttribute UserModel user) {
        Long id = repository.findByEmail(user.getEmail()).getId();
        user.setId(id);
        tokenService.register(user);
        return "redirect:/index?recover=true";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute UserModel user, Model model, HttpSession session) {
        try {
            model.addAttribute("user", userDetailsService.register(user));
            session.setAttribute("warning", "You have to confirm your email first.");
        } catch (UserAlreadyExistsException e) {
            model.addAttribute("error ".concat(String.valueOf(e)));
            session.setAttribute("error", "Your email/username is invalid");
            return "redirect:/register?error=true";
        }

        String token = user.getToken().getToken();

        emailSender.sendEmail(user.getUsername(), user.getEmail(), token, "Confirm your account");

        return "redirect:/login?confirm=true";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute UserModel user, HttpSession session) {

        String newPassword = passwordEncoder.encode(user.getPassword());
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userInSession = userServiceImpl.getUsernameFromPrincipal(principal);
        Long userId = repository.findByUsername(userInSession).getId();
        repository.update(userId, user.getUsername(), user.getEmail(), newPassword);

        return "redirect:/login";
    }

    @PostMapping("/delete")
    public String delete() {
        // make sure user calling api is same as the one being deleted
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userInSession = userServiceImpl.getUsernameFromPrincipal(principal);
        System.out.println("deleting account server side");
        UserModel user = userDetailsService.findByUsername(userInSession);
        userDetailsService.delete(user);
        return "redirect:/logout";
    }
}