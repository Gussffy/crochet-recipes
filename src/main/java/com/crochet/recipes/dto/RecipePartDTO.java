package com.crochet.recipes.dto;

import com.crochet.recipes.config.NoMongoInjection;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipePartDTO {

    @NotNull(message = "Ordem da parte é obrigatória")
    @Min(value = 1, message = "Ordem deve ser maior que 0")
    private Integer order;

    @NotBlank(message = "Título da parte é obrigatório")
    @NoMongoInjection
    private String title;

    @NotEmpty(message = "Pelo menos uma volta (round) é obrigatória")
    @Valid
    private List<RoundDTO> rounds;

    private String imageBase64;

    private String imageContentType;
}
