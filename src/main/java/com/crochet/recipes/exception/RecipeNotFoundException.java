package com.crochet.recipes.exception;

public class RecipeNotFoundException extends RuntimeException {

    public RecipeNotFoundException(String id) {
        super("Receita não encontrada com o ID: " + id);
    }
}
