package com.crochet.recipes.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoundDTO {

    @NotNull(message = "Número da volta é obrigatório")
    @Min(value = 1, message = "Número da volta deve ser maior que 0")
    private Integer roundNumber;

    @NotBlank(message = "Descrição da volta é obrigatória")
    private String description;
}

