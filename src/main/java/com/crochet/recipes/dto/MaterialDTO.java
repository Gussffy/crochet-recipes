package com.crochet.recipes.dto;

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
    private String name;

    @NotBlank(message = "Quantidade do material é obrigatória")
    private String quantity;

    private String color;

    private String notes;
}
