package com.crochet.recipes.exception;

import org.springframework.http.HttpStatus;

public class DatabaseException extends CrochetException {

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.SERVICE_UNAVAILABLE; // 503
    }
}

