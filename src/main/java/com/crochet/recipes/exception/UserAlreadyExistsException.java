package com.crochet.recipes.exception;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends CrochetException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.CONFLICT;
    }
}

