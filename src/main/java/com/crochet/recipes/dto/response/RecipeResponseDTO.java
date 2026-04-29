package com.crochet.recipes.dto.response;

import com.crochet.recipes.dto.request.MaterialDTO;
import com.crochet.recipes.dto.request.RecipePartDTO;

import java.time.LocalDateTime;
import java.util.List;

public record RecipeResponseDTO(
    String id,
    String name,
    String description,
    String authorName,
    List<MaterialDTO> materials,
    List<RecipePartDTO> parts,
    String coverImageBase64,
    String coverImageContentType,
    List<String> tags,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
