package com.crochet.recipes.dto;

import com.crochet.recipes.config.NoMongoInjection;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialDTO {

    @NotBlank(message = "Nome do material é obrigatório")
    @NoMongoInjection
    private String name;

    @NotBlank(message = "Quantidade do material é obrigatória")
    @NoMongoInjection
    private String quantity;

    @NoMongoInjection
    private String color;

    @NoMongoInjection
    private String notes;
}
