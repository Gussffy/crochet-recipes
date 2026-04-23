package com.crochet.recipes.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipePart {

    private Integer order;

    private String title;

    private String instructions;

    private String imageBase64;

    private String imageContentType;
}
