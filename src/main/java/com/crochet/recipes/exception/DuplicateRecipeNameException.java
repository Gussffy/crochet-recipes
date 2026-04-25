package com.crochet.recipes.exception;

import org.springframework.http.HttpStatus;

public class DuplicateRecipeNameException extends CrochetException {

    public DuplicateRecipeNameException(String name) {
        super("Já existe uma receita com o nome: '" + name + "'");
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.CONFLICT; // 409
    }
}

