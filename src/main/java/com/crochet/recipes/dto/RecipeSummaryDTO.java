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
public class RecipeSummaryDTO {

    private String id;
    private String name;
    private String description;
    private String authorName;
    private int totalParts;
    private int totalMaterials;
    private List<String> tags;
    private String coverImageBase64;
    private String coverImageContentType;
    private LocalDateTime createdAt;
}
