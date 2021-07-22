package com.roman.tipear.implementation;

import com.roman.tipear.model.entity.UserModel;
import com.roman.tipear.model.exception.UserAlreadyExistsException;
import com.roman.tipear.repository.UserRepository;
import com.roman.tipear.service.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Mock
    private TokenService tokenService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private UserModel existingUser = new UserModel();
    private UserModel newUser = new UserModel();

    @BeforeEach
    public void setUpMock() {
        MockitoAnnotations.openMocks(this);

        existingUser.setId(1L);
        existingUser.setUsername("existingUser");
        existingUser.setEmail("existinguser@gmail.com");
        existingUser.setPassword("existing");

        newUser.setId(2L);
        newUser.setUsername("newUser");
        newUser.setEmail("newuser@gmail.com");
        newUser.setPassword("new");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByUsername("existingUser")).thenReturn(existingUser);
        when(userRepository.findByUsername("existinguser@gmail.com")).thenReturn(existingUser);
    }

    @DisplayName("It should return an exception")
    @Test
    void tryRegisterExistingUser() {
        Exception exception = assertThrows(UserAlreadyExistsException.class, () -> {
            userService.register(existingUser);
        });

        String expectedMessage = "Username with that email/username already exists";
        String message = exception.getMessage();
        assertTrue(message.equals(expectedMessage));
    }

    @DisplayName("It should not change the password")
    @Test
    void updateWithoutPassword() {
        String passwordBefore = existingUser.getPassword();
        existingUser.setPassword("");
        userService.updateUserData(existingUser, existingUser.getId());
        existingUser.setPassword("existing");
        assertEquals(passwordBefore, existingUser.getPassword());
    }

    @DisplayName("It should change the password")
    @Test
    void updateWithPassword() {
        String passwordBefore = existingUser.getPassword();
        existingUser.setPassword("123131");
        userService.updateUserData(existingUser, existingUser.getId());

        Boolean passwordsAreDifferent = passwordBefore != existingUser.getPassword();
        assertTrue(passwordsAreDifferent);
    }

}