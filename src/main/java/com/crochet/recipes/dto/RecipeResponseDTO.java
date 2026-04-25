package com.crochet.recipes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeResponseDTO {

    private String id;
    private String name;
    private String description;
    private String authorName;
    private List<MaterialDTO> materials;
    private List<RecipePartDTO> parts;
    private String coverImageBase64;
    private String coverImageContentType;
    private List<String> tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
