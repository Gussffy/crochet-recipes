package com.crochet.recipes.exception;

public class UserNotFoundException extends CrochetException {

    public UserNotFoundException(String email) {
        super("Usuário não encontrado: " + email);
    }

    @Override
    public org.springframework.http.HttpStatus getHttpStatus() {
        return org.springframework.http.HttpStatus.NOT_FOUND;
    }
}

