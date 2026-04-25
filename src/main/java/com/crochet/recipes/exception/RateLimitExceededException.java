package com.crochet.recipes.exception;

import org.springframework.http.HttpStatus;

public class RateLimitExceededException extends CrochetException {

    public RateLimitExceededException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.TOO_MANY_REQUESTS; // 429
    }
}

