package com.crochet.recipes.dto.request;

import com.crochet.recipes.config.NoMongoInjection;
import jakarta.validation.constraints.NotBlank;

public record MaterialDTO(
    @NotBlank(message = "Nome do material é obrigatório")
    @NoMongoInjection
    String name,

    @NotBlank(message = "Quantidade do material é obrigatória")
    @NoMongoInjection
    String quantity,

    @NoMongoInjection
    String color,

    @NoMongoInjection
    String notes
) {}
