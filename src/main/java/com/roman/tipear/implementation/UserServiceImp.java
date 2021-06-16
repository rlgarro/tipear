package com.roman.tipear.implementation;

import com.roman.tipear.model.entity.UserModel;
import com.roman.tipear.model.exception.UserAlreadyExistsException;
import com.roman.tipear.repository.UserRepository;
import com.roman.tipear.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service
public class UserServiceImp implements UserService {

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
            return userRepository.save(user);
        }
    }

    public Boolean userAlreadyExists(String username, String email) {
        UserModel userByUsername = userRepository.findByUsername(username);
        UserModel userByEmail = userRepository.findByEmail(email);

        Boolean oneIsDup = userByEmail != null || userByUsername != null;

        return oneIsDup;
    }
}