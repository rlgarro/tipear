package com.roman.tipear.controller;

import com.roman.tipear.email.EmailSenderService;
import com.roman.tipear.implementation.UserServiceImpl;
import com.roman.tipear.model.entity.TokenModel;
import com.roman.tipear.model.entity.UserModel;
import com.roman.tipear.model.exception.UserAlreadyExistsException;
import com.roman.tipear.repository.TokenRepository;
import com.roman.tipear.repository.TypingTestRepository;
import com.roman.tipear.repository.UserRepository;
import com.roman.tipear.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository repository;

    @Autowired
    private TypingTestRepository testRepository;

    @Autowired
    private TokenRepository tokenRepo;

    @Autowired
    EmailSenderService emailSender;


    // get mappings
    @GetMapping("/u/{username}")
    public String userProfile(Authentication auth, HttpSession session, @PathVariable String username, Model model) {

        // check that the username on the variable exists
        Boolean userExists = userService.findByUsername(username) != null;
        if (!userExists) {
            return "404";
        }

        model = userService.setUserProfileAuthBased(model, session, username);

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
    public String confirmUser(Model model, @PathVariable String token) {
        TokenModel tokenEntity = tokenService.findByToken(token);
        userService.confirmUserByToken(tokenEntity);

        return "redirect:/login";
    }

    @GetMapping("/recover/password")
    public String recoverPasswordPage() {
        return "recoverPassword";
    }

    @GetMapping("/recover")
    public String recoverPage() {
        return "recover";
    }

    @GetMapping("/recover/confirm/{token}")
    public String recoverPasswordPage(HttpSession session, @PathVariable String token) {
        TokenModel tokenEntity = tokenService.findByToken(token);
        UserModel user = tokenEntity.getUser();
        session.setAttribute("email", user.getEmail());

       return "recoverPassword";
    }

    // post mappings
    @PostMapping("/recover")
    public String createRecoverToken(@ModelAttribute UserModel user) {
        return userService.createRecoverToken(user);
    }

    @PostMapping("/recover/password")
    public String passwordChange(@ModelAttribute UserModel userNewPassword, HttpSession session) {

        userService.passwordChange(userNewPassword, session);
        return "redirect:/login";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute UserModel user, Model model, HttpSession session) {
        try {
            model.addAttribute("user", userService.register(user));
            session.setAttribute("warning", "You have to confirm your email first.");
        } catch (UserAlreadyExistsException e) {
            model.addAttribute("error ".concat(String.valueOf(e)));
            session.setAttribute("error", "Your email/username is invalid");
            return "redirect:/register?error=true";
        }

        String token = user.getToken().getToken();

        String url = "http://localhost:8080/confirm/".concat(token);
        emailSender.sendEmail("activation", user.getUsername(), user.getEmail(), url, "Confirm your account");

        return "redirect:/login?confirm=true";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute UserModel user, HttpSession session) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userInSession = userService.getUsernameFromPrincipal(principal);
        Long userId = userService.findByUsername(userInSession).getId();
        session.invalidate();


        userService.updateUserData(user, userId);
        return "redirect:/login";
    }

    @PostMapping("/delete")
    public String delete() {

        userService.delete();
        return "redirect:/logout";
    }
}
