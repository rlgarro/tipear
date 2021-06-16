package com.roman.tipear.model.exception;

import javax.naming.AuthenticationException;

public class UserAlreadyExistsException extends AuthenticationException {

    public UserAlreadyExistsException(final String message) {
        super(message);
    }
}
