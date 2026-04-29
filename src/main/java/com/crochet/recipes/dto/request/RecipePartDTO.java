package com.crochet.recipes.dto.request;

import com.crochet.recipes.config.NoMongoInjection;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record RecipePartDTO(
    @NotNull(message = "Ordem da parte é obrigatória")
    @Min(value = 1, message = "Ordem deve ser maior que 0")
    Integer order,

    @NotBlank(message = "Título da parte é obrigatório")
    @NoMongoInjection
    String title,

    @NotEmpty(message = "Pelo menos uma volta (round) é obrigatória")
    @Valid
    List<RoundDTO> rounds,

    String imageBase64,

    String imageContentType
) {}
