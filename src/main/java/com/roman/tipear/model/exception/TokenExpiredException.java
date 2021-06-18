package com.roman.tipear.model.exception;

import antlr.MismatchedTokenException;
import javassist.NotFoundException;

public class TokenExpiredException extends NotFoundException {

    public TokenExpiredException(String msg) {
        super(msg);
    }

    public TokenExpiredException(String msg, Exception e) {
        super(msg, e);
    }
}
