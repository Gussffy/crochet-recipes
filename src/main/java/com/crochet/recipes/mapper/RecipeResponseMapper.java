package com.crochet.recipes.mapper;

import com.crochet.recipes.dto.request.MaterialDTO;
import com.crochet.recipes.dto.request.RecipePartDTO;
import com.crochet.recipes.dto.request.RoundDTO;
import com.crochet.recipes.dto.response.RecipeResponseDTO;
import com.crochet.recipes.dto.response.RecipeSummaryDTO;
import com.crochet.recipes.model.embedded.Material;
import com.crochet.recipes.model.Recipe;
import com.crochet.recipes.model.embedded.RecipePart;
import com.crochet.recipes.model.embedded.Round;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class RecipeResponseMapper {

    public RecipeResponseDTO toResponseDTO(Recipe recipe) {
        log.debug("Convertendo Recipe para RecipeResponseDTO: {}", recipe.getId());
        return new RecipeResponseDTO(
                recipe.getId(),
                recipe.getName(),
                recipe.getDescription(),
                recipe.getAuthorName(),
                toMaterialDTOList(recipe.getMaterials()),
                toPartDTOList(recipe.getParts()),
                recipe.getCoverImageBase64(),
                recipe.getCoverImageContentType(),
                recipe.getTags(),
                recipe.getCreatedAt(),
                recipe.getUpdatedAt()
        );
    }

    public RecipeSummaryDTO toSummaryDTO(Recipe recipe) {
        log.debug("Convertendo Recipe para RecipeSummaryDTO: {}", recipe.getId());
        return new RecipeSummaryDTO(
                recipe.getId(),
                recipe.getName(),
                recipe.getDescription(),
                recipe.getAuthorName(),
                recipe.getParts() != null ? recipe.getParts().size() : 0,
                recipe.getMaterials() != null ? recipe.getMaterials().size() : 0,
                recipe.getTags(),
                recipe.getCoverImageBase64(),
                recipe.getCoverImageContentType(),
                recipe.getCreatedAt()
        );
    }

    private List<MaterialDTO> toMaterialDTOList(List<Material> materials) {
        if (materials == null) return List.of();
        return materials.stream()
                .map(m -> new MaterialDTO(m.getName(), m.getQuantity(), m.getColor(), m.getNotes()))
                .collect(Collectors.toList());
    }

    private List<RecipePartDTO> toPartDTOList(List<RecipePart> parts) {
        if (parts == null) return List.of();
        return parts.stream()
                .map(p -> new RecipePartDTO(
                        p.getOrder(),
                        p.getTitle(),
                        toRoundDTOList(p.getRounds()),
                        p.getImageBase64(),
                        p.getImageContentType()
                ))
                .collect(Collectors.toList());
    }

    private List<RoundDTO> toRoundDTOList(List<Round> rounds) {
        if (rounds == null) return List.of();
        return rounds.stream()
                .map(r -> new RoundDTO(r.getRoundNumber(), r.getDescription()))
                .collect(Collectors.toList());
    }
}

