package com.crochet.recipes.dto;

import com.crochet.recipes.config.NoMongoInjection;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeRequestDTO {

    @NotBlank(message = "Nome da receita é obrigatório")
    @NoMongoInjection
    private String name;

    @NotBlank(message = "Descrição da receita é obrigatória")
    @NoMongoInjection
    private String description;

    @NotBlank(message = "Nome do autor é obrigatório")
    @NoMongoInjection
    private String authorName;


    @NotEmpty(message = "A receita deve ter pelo menos um material")
    @Valid
    private List<MaterialDTO> materials;

    @NotEmpty(message = "A receita deve ter pelo menos uma parte")
    @Valid
    private List<RecipePartDTO> parts;

    private String coverImageBase64;

    private String coverImageContentType;

    private List<String> tags;
}
