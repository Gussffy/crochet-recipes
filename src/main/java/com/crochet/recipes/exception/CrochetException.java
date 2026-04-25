package com.crochet.recipes.exception;

import org.springframework.http.HttpStatus;

public abstract class CrochetException extends RuntimeException {

    public CrochetException(String message) {
        super(message);
    }

    public CrochetException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract HttpStatus getHttpStatus();
}

