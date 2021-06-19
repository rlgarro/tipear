package com.roman.tipear.implementation;

import com.roman.tipear.model.entity.RegTokenModel;
import com.roman.tipear.model.entity.UserModel;
import com.roman.tipear.model.exception.TokenExpiredException;
import com.roman.tipear.model.exception.UserAlreadyExistsException;
import com.roman.tipear.repository.UserRepository;
import com.roman.tipear.service.RegTokenService;
import com.roman.tipear.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.SecureRandom;
import java.time.LocalDate;

@Transactional
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    RegTokenService tokenService;

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
            /*System.out.println("USER ID: " + user.getId());
            System.out.println("TOKEN ID: " + token.getId());
             */
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            System.out.println("USER ID BEFORE SAVING: " + user.getId());
            UserModel savedUser  = userRepository.save(user);
            RegTokenModel token = tokenService.register(savedUser);
            savedUser.setToken(token);
            return savedUser;
        }
    }

    @Override
    public void confirmUserByToken(RegTokenModel token) throws TokenExpiredException {

        Boolean tokenExpired = token.getExpiresAt().isBefore(LocalDate.now());
        if (tokenExpired) {
            System.out.println("EXPIRO AMIGO");
            throw new TokenExpiredException("token already expired");
        } else if(!tokenExpired){
            UserModel user = token.getUser();
            System.out.println("USERNAME: " + user.getUsername());
            user.setActive(true);
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