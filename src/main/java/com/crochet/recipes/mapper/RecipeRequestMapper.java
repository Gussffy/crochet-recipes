package com.crochet.recipes.mapper;

import com.crochet.recipes.dto.request.MaterialDTO;
import com.crochet.recipes.dto.request.RecipePartDTO;
import com.crochet.recipes.dto.request.RecipeRequestDTO;
import com.crochet.recipes.dto.request.RoundDTO;
import com.crochet.recipes.model.embedded.Material;
import com.crochet.recipes.model.Recipe;
import com.crochet.recipes.model.embedded.RecipePart;
import com.crochet.recipes.model.embedded.Round;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class RecipeRequestMapper {

    public Recipe toModel(RecipeRequestDTO dto) {
        log.debug("Convertendo RecipeRequestDTO para Recipe: {}", dto.name());
        return Recipe.builder()
                .name(dto.name())
                .description(dto.description())
                .authorName(dto.authorName())
                .materials(toMaterialList(dto.materials()))
                .parts(toPartList(dto.parts()))
                .coverImageBase64(dto.coverImageBase64())
                .coverImageContentType(dto.coverImageContentType())
                .tags(dto.tags())
                .build();
    }

    public void updateModel(Recipe recipe, RecipeRequestDTO dto) {
        log.debug("Atualizando Recipe com dados de RecipeRequestDTO");
        recipe.setName(dto.name());
        recipe.setDescription(dto.description());
        recipe.setAuthorName(dto.authorName());
        recipe.setMaterials(toMaterialList(dto.materials()));
        recipe.setParts(toPartList(dto.parts()));
        recipe.setCoverImageBase64(dto.coverImageBase64());
        recipe.setCoverImageContentType(dto.coverImageContentType());
        recipe.setTags(dto.tags());
    }

    private List<Material> toMaterialList(List<MaterialDTO> dtos) {
        if (dtos == null) return List.of();
        return dtos.stream()
                .map(dto -> Material.builder()
                        .name(dto.name())
                        .quantity(dto.quantity())
                        .color(dto.color())
                        .notes(dto.notes())
                        .build())
                .collect(Collectors.toList());
    }

    private List<RecipePart> toPartList(List<RecipePartDTO> dtos) {
        if (dtos == null) return List.of();
        return dtos.stream()
                .map(dto -> RecipePart.builder()
                        .order(dto.order())
                        .title(dto.title())
                        .rounds(toRoundList(dto.rounds()))
                        .imageBase64(dto.imageBase64())
                        .imageContentType(dto.imageContentType())
                        .build())
                .sorted(Comparator.comparingInt(RecipePart::getOrder))
                .collect(Collectors.toList());
    }

    private List<Round> toRoundList(List<RoundDTO> dtos) {
        if (dtos == null) return List.of();
        return dtos.stream()
                .map(dto -> Round.builder()
                        .roundNumber(dto.roundNumber())
                        .description(dto.description())
                        .build())
                .sorted(Comparator.comparingInt(Round::getRoundNumber))
                .collect(Collectors.toList());
    }
}
