package com.roman.tipear.implementation;

import com.roman.tipear.email.EmailSenderService;
import com.roman.tipear.model.entity.TokenModel;
import com.roman.tipear.model.entity.UserModel;
import com.roman.tipear.model.exception.UserAlreadyExistsException;
import com.roman.tipear.repository.TokenRepository;
import com.roman.tipear.repository.UserRepository;
import com.roman.tipear.service.TokenService;
import com.roman.tipear.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Transactional
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    EmailSenderService emailSenderService;

    @Autowired
    TokenService tokenService;

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
            // if token expired => delete user and token, send email saying time expired
            emailSenderService.sendEmail("deletion", "", user.getEmail(), "", "Account registration expired");
            userRepository.delete(user);
        } else {
            user.setActive(true);
            tokenRepo.delete(token);
        }
    }

    public Boolean userAlreadyExists(String username, String email) {
        UserModel userByUsername = userRepository.findByUsername(username);
        UserModel userByEmail = userRepository.findByEmail(email);

        Boolean oneIsDup = userByEmail != null || userByUsername != null;

        return oneIsDup;
    }

    public String getUsernameFromPrincipal(Object principal) {
        return ((UserDetails) principal).getUsername();
    }

    @Override
    public void delete(UserModel user) {
        userRepository.delete(user);
    }

}