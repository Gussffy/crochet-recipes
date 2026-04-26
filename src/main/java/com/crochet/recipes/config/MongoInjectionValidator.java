package com.crochet.recipes.config;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MongoInjectionValidator implements ConstraintValidator<NoMongoInjection, String> {

    private static final String MONGO_INJECTION_PATTERN = ".*[{$}();|&*\"'`\\\\].*";

    @Override
    public void initialize(NoMongoInjection annotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        if (value.matches(MONGO_INJECTION_PATTERN)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                context.getDefaultConstraintMessageTemplate()
            ).addConstraintViolation();
            return false;
        }

        return true;
    }
}
