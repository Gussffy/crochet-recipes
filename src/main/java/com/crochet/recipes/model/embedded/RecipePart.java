package com.crochet.recipes.model.embedded;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipePart {

    private Integer order;

    private String title;

    private List<Round> rounds;

    private String imageBase64;

    private String imageContentType;
}
