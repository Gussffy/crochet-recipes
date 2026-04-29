package com.crochet.recipes.dto.request;

import com.crochet.recipes.config.NoMongoInjection;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record RecipeRequestDTO(
    @NotBlank(message = "Nome da receita é obrigatório")
    @NoMongoInjection
    String name,

    @NotBlank(message = "Descrição da receita é obrigatória")
    @NoMongoInjection
    String description,

    @NotBlank(message = "Nome do autor é obrigatório")
    @NoMongoInjection
    String authorName,

    @NotEmpty(message = "A receita deve ter pelo menos um material")
    @Valid
    List<MaterialDTO> materials,

    @NotEmpty(message = "A receita deve ter pelo menos uma parte")
    @Valid
    List<RecipePartDTO> parts,

    String coverImageBase64,

    String coverImageContentType,

    List<String> tags
) {}
