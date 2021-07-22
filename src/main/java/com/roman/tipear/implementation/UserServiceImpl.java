package com.roman.tipear.implementation;

import com.roman.tipear.email.EmailSenderService;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Transactional
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    EmailSenderService emailSenderService;

    @Autowired
    TokenService tokenService;

    @Autowired
    TypingTestRepository testRepository;

    @Autowired
    TokenRepository tokenRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserModel findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserModel findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserModel register(UserModel user) throws UserAlreadyExistsException {

        // check for duplicates
        Boolean userAlreadyExists = userAlreadyExists(user.getUsername(), user.getEmail());
        if (userAlreadyExists) {
            throw new UserAlreadyExistsException("Username with that email/username already exists");
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            UserModel savedUser  = userRepository.save(user);
            TokenModel token = tokenService.register(savedUser);
            savedUser.setToken(token);
            return savedUser;
        }
    }

    @Override
    public void confirmUserByToken(TokenModel token) {

        Boolean tokenExpired = token.getExpiresAt().isBefore(LocalDateTime.now());
        UserModel user = token.getUser();
        if (tokenExpired) {
            emailSenderService.sendEmail("deletion", "", user.getEmail(), "", "Account registration expired");
            userRepository.delete(user);
        } else {
            user.setActive(true);
            tokenRepo.delete(token);
        }
    }

    @Override
    public void delete() {
        // make sure user calling api is same as the one being deleted
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userInSession = this.getUsernameFromPrincipal(principal);
        UserModel user = this.findByUsername(userInSession);
        userRepository.delete(user);
    }

    @Override
    public Model setUserProfileAuthBased(Model model, HttpSession session, String username) {
        //HashMap<String, Object> configuredProperties = new HashMap<>();

        // check if user session is the same as user in path variable
        UserModel userRequested = this.findByUsername(username);
        session.setAttribute("userRequested", userRequested);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userInSession = "";

        if (principal instanceof UserDetails) {
            userInSession = this.getUsernameFromPrincipal(principal);
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

        return model;
    }

    @Override
    public void updateUserData(UserModel user, Long userId) {

        if (user.getPassword().equals("")) {
            userRepository.parcialUpdate(userId, user.getUsername(), user.getEmail());
        } else {
            String newPassword = passwordEncoder.encode(user.getPassword());
            userRepository.fullUpdate(userId, user.getUsername(), user.getEmail(), newPassword);
        }
    }

    @Override
    public String createRecoverToken(UserModel user) {
        String redirectPath = "";

        Boolean userExists = this.userAlreadyExists("", user.getEmail());

        // create only if that email exists, user is activated, user has 0 tokens.
        if (userExists) {

            user = userRepository.findByEmail(user.getEmail());
            Boolean userHasNoTokens = tokenService.tokenByEmail(user.getEmail());

            if (user.userIsActive() && userHasNoTokens) {

                TokenModel token = tokenService.register(user);
                String url = "http://localhost:8080/recover/confirm/" + token.getToken();
                emailSenderService.sendEmail("password", user.getUsername(), user.getEmail(), url, "Reset your password");
                redirectPath = "redirect:/?recover=true";
            }

            // get which of the conditions wasn't met
            else if (user.userIsActive() && !userHasNoTokens) {
                redirectPath =  "redirect:/recover?token=true";
            } else {
                redirectPath =  "redirect:/recover?active=true";
            }
        } else {
            redirectPath = "redirect:/recover?email=true";
        }
        return redirectPath;
    }

    @Override
    public void passwordChange(UserModel userWithNewPassword, HttpSession session) {
        UserModel user = userRepository.findByEmail((String) session.getAttribute("email"));
        String password = passwordEncoder.encode(userWithNewPassword.getPassword());
        user.setPassword(password);
        userRepository.fullUpdate(user.getId(), user.getUsername(), user.getEmail(), user.getPassword());
        tokenRepo.delete(user.getToken());
        session.removeAttribute("email");
    }

    public Boolean userAlreadyExists(String username, String email) {
        UserModel userByUsername = userRepository.findByUsername(username);
        UserModel userByEmail = userRepository.findByEmail(email);

        Boolean exists = userByEmail != null || userByUsername != null;

        return exists;
    }

    public String getUsernameFromPrincipal(Object principal) {
        return ((UserDetails) principal).getUsername();
    }
}