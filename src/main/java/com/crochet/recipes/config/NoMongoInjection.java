package com.crochet.recipes.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NoMongoInjection {
    String message() default "Campo contém caracteres suspeitos que podem representar injeção MongoDB";
}
