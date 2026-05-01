package com.crochet.recipes.exception;

public class JwtAuthenticationException extends CrochetException {

    public JwtAuthenticationException(String message) {
        super(message);
    }

    public JwtAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public org.springframework.http.HttpStatus getHttpStatus() {
        return org.springframework.http.HttpStatus.UNAUTHORIZED;
    }
}

