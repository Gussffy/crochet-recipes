package com.crochet.recipes.dto.request;

import com.crochet.recipes.config.NoMongoInjection;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RoundDTO(
    @NotNull(message = "Número da volta é obrigatório")
    @Min(value = 1, message = "Número da volta deve ser maior que 0")
    Integer roundNumber,

    @NotBlank(message = "Descrição da volta é obrigatória")
    @NoMongoInjection
    String description
) {}

