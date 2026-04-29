package com.crochet.recipes.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record RecipeSummaryDTO(
    String id,
    String name,
    String description,
    String authorName,
    int totalParts,
    int totalMaterials,
    List<String> tags,
    String coverImageBase64,
    String coverImageContentType,
    LocalDateTime createdAt
) {}
