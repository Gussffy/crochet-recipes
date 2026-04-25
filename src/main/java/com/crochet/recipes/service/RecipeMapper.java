package com.crochet.recipes.service;

import com.crochet.recipes.dto.*;
import com.crochet.recipes.model.Material;
import com.crochet.recipes.model.Recipe;
import com.crochet.recipes.model.RecipePart;
import com.crochet.recipes.model.Round;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RecipeMapper {

    public Recipe toModel(RecipeRequestDTO dto) {
        return Recipe.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .authorName(dto.getAuthorName())
                .difficulty(dto.getDifficulty())
                .materials(toMaterialList(dto.getMaterials()))
                .parts(toPartList(dto.getParts()))
                .coverImageBase64(dto.getCoverImageBase64())
                .coverImageContentType(dto.getCoverImageContentType())
                .tags(dto.getTags())
                .build();
    }

    public RecipeResponseDTO toResponseDTO(Recipe recipe) {
        return RecipeResponseDTO.builder()
                .id(recipe.getId())
                .name(recipe.getName())
                .description(recipe.getDescription())
                .authorName(recipe.getAuthorName())
                .difficulty(recipe.getDifficulty())
                .materials(toMaterialDTOList(recipe.getMaterials()))
                .parts(toPartDTOList(recipe.getParts()))
                .coverImageBase64(recipe.getCoverImageBase64())
                .coverImageContentType(recipe.getCoverImageContentType())
                .tags(recipe.getTags())
                .createdAt(recipe.getCreatedAt())
                .updatedAt(recipe.getUpdatedAt())
                .build();
    }

    public RecipeSummaryDTO toSummaryDTO(Recipe recipe) {
        return RecipeSummaryDTO.builder()
                .id(recipe.getId())
                .name(recipe.getName())
                .description(recipe.getDescription())
                .authorName(recipe.getAuthorName())
                .difficulty(recipe.getDifficulty())
                .totalParts(recipe.getParts() != null ? recipe.getParts().size() : 0)
                .totalMaterials(recipe.getMaterials() != null ? recipe.getMaterials().size() : 0)
                .tags(recipe.getTags())
                .coverImageBase64(recipe.getCoverImageBase64())
                .coverImageContentType(recipe.getCoverImageContentType())
                .createdAt(recipe.getCreatedAt())
                .build();
    }

    public void updateModel(Recipe recipe, RecipeRequestDTO dto) {
        recipe.setName(dto.getName());
        recipe.setDescription(dto.getDescription());
        recipe.setAuthorName(dto.getAuthorName());
        recipe.setDifficulty(dto.getDifficulty());
        recipe.setMaterials(toMaterialList(dto.getMaterials()));
        recipe.setParts(toPartList(dto.getParts()));
        recipe.setCoverImageBase64(dto.getCoverImageBase64());
        recipe.setCoverImageContentType(dto.getCoverImageContentType());
        recipe.setTags(dto.getTags());
    }

    private List<Material> toMaterialList(List<MaterialDTO> dtos) {
        if (dtos == null) return List.of();
        return dtos.stream()
                .map(dto -> Material.builder()
                        .name(dto.getName())
                        .quantity(dto.getQuantity())
                        .color(dto.getColor())
                        .notes(dto.getNotes())
                        .build())
                .collect(Collectors.toList());
    }

    private List<MaterialDTO> toMaterialDTOList(List<Material> materials) {
        if (materials == null) return List.of();
        return materials.stream()
                .map(m -> MaterialDTO.builder()
                        .name(m.getName())
                        .quantity(m.getQuantity())
                        .color(m.getColor())
                        .notes(m.getNotes())
                        .build())
                .collect(Collectors.toList());
    }

    private List<RecipePart> toPartList(List<RecipePartDTO> dtos) {
        if (dtos == null) return List.of();
        return dtos.stream()
                .map(dto -> RecipePart.builder()
                        .order(dto.getOrder())
                        .title(dto.getTitle())
                        .rounds(toRoundList(dto.getRounds()))
                        .imageBase64(dto.getImageBase64())
                        .imageContentType(dto.getImageContentType())
                        .build())
                .sorted(Comparator.comparingInt(RecipePart::getOrder))
                .collect(Collectors.toList());
    }

    private List<RecipePartDTO> toPartDTOList(List<RecipePart> parts) {
        if (parts == null) return List.of();
        return parts.stream()
                .map(p -> RecipePartDTO.builder()
                        .order(p.getOrder())
                        .title(p.getTitle())
                        .rounds(toRoundDTOList(p.getRounds()))
                        .imageBase64(p.getImageBase64())
                        .imageContentType(p.getImageContentType())
                        .build())
                .collect(Collectors.toList());
    }

    private List<Round> toRoundList(List<RoundDTO> dtos) {
        if (dtos == null) return List.of();
        return dtos.stream()
                .map(dto -> Round.builder()
                        .roundNumber(dto.getRoundNumber())
                        .description(dto.getDescription())
                        .build())
                .sorted(Comparator.comparingInt(Round::getRoundNumber))
                .collect(Collectors.toList());
    }

    private List<RoundDTO> toRoundDTOList(List<Round> rounds) {
        if (rounds == null) return List.of();
        return rounds.stream()
                .map(r -> RoundDTO.builder()
                        .roundNumber(r.getRoundNumber())
                        .description(r.getDescription())
                        .build())
                .collect(Collectors.toList());
    }
}
