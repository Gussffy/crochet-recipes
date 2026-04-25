package com.crochet.recipes.exception;

import org.springframework.http.HttpStatus;

public class InvalidImageException extends CrochetException {

    public InvalidImageException(String reason) {
        super("Imagem inválida: " + reason);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST; // 400
    }
}

