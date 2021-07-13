package com.roman.tipear.controller;

import com.roman.tipear.email.EmailSenderService;
import com.roman.tipear.implementation.UserServiceImpl;
import com.roman.tipear.model.entity.TokenModel;
import com.roman.tipear.model.entity.TypingTest;
import com.roman.tipear.model.entity.UserModel;
import com.roman.tipear.model.exception.UserAlreadyExistsException;
import com.roman.tipear.repository.TokenRepository;
import com.roman.tipear.repository.TypingTestRepository;
import com.roman.tipear.repository.UserRepository;
import com.roman.tipear.service.TokenService;
import com.roman.tipear.service.UserService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

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
    private TypingTestRepository testRepository;

    @Autowired
    private TokenRepository tokenRepo;

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

        try {
            List<TypingTest> testsList = testRepository.findAllByUserId(userRequested.getId());

            if (testsList.size() > 0) {
                int average = 0;
                for(TypingTest typingTest: testsList) {
                    average += typingTest.getScore();
                }
                average = average / testsList.size();
                if (testsList.size() >= 4) {
                    testsList = testsList.subList(0, 4);
                }

                // add the last 4 tests to the model
                model.addAttribute("testsList", testsList);
                model.addAttribute("averageWPM", average);
                model.addAttribute("testsTaken", testsList.size());
                model.addAttribute("hasTests", true);
            } else {
                model.addAttribute("hasTests", null);
            }
        } catch (NotFoundException exception) {
            model.addAttribute("hasTests", null);
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
    public String confirmUser(Model model, @PathVariable String token) {
        TokenModel tokenEntity = tokenService.findByToken(token);

        userDetailsService.confirmUserByToken(tokenEntity);

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
        Boolean userExists = userServiceImpl.userAlreadyExists("", user.getEmail());

        // create only if that email exists, user is activated, user has 0 tokens.
        if (userExists) {

            user = repository.findByEmail(user.getEmail());
            Boolean userHasNoTokens = tokenService.tokenByEmail(user.getEmail());

            if (user.userIsActive() && userHasNoTokens) {

                TokenModel token = tokenService.register(user);
                String url = "http://localhost:8080/recover/confirm/" + token.getToken();
                emailSender.sendEmail("password", user.getUsername(), user.getEmail(), url, "Reset your password");
                return "redirect:/?recover=true";
            }

            // get which of the conditions wasn't met
            else if (user.userIsActive() && !userHasNoTokens) {
                return "redirect:/recover?token=true";
            } else {
                return "redirect:/recover?active=true";
            }
        }
        return "redirect:/recover?email=true";
    }

    @PostMapping("/recover/password")
    public String passwordChange(@ModelAttribute UserModel userNewPassword, HttpSession session) {

        UserModel user = repository.findByEmail((String) session.getAttribute("email"));
        String password = passwordEncoder.encode(userNewPassword.getPassword());
        user.setPassword(password);
        repository.update(user.getId(), user.getUsername(), user.getEmail(), user.getPassword());
        tokenRepo.delete(user.getToken());
        session.removeAttribute("email");
        return "redirect:/login";
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


        String url = "http://localhost:8080/confirm/".concat(token);
        emailSender.sendEmail("activation", user.getUsername(), user.getEmail(), url, "Confirm your account");

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
        UserModel user = userDetailsService.findByUsername(userInSession);
        userDetailsService.delete(user);
        return "redirect:/logout";
    }
}
