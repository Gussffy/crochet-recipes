package com.crochet.recipes.service;

import com.crochet.recipes.dto.RecipeRequestDTO;
import com.crochet.recipes.dto.RecipeResponseDTO;
import com.crochet.recipes.dto.RecipeSummaryDTO;
import com.crochet.recipes.exception.RecipeNotFoundException;
import com.crochet.recipes.model.Recipe;
import com.crochet.recipes.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;

    public RecipeResponseDTO createRecipe(RecipeRequestDTO requestDTO) {
        log.info("Criando nova receita: {}", requestDTO.getName());
        validateDifficulty(requestDTO.getDifficulty());

        Recipe recipe = recipeMapper.toModel(requestDTO);
        Recipe saved = recipeRepository.save(recipe);

        log.info("Receita criada com sucesso. ID: {}", saved.getId());
        return recipeMapper.toResponseDTO(saved);
    }

    public RecipeResponseDTO getRecipeById(String id) {
        log.info("Buscando receita por ID: {}", id);
        Recipe recipe = findRecipeOrThrow(id);
        return recipeMapper.toResponseDTO(recipe);
    }

    public List<RecipeSummaryDTO> getAllRecipes() {
        log.info("Listando todas as receitas");
        return recipeRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(recipeMapper::toSummaryDTO)
                .collect(Collectors.toList());
    }

    public RecipeResponseDTO updateRecipe(String id, RecipeRequestDTO requestDTO) {
        log.info("Atualizando receita ID: {}", id);
        validateDifficulty(requestDTO.getDifficulty());

        Recipe recipe = findRecipeOrThrow(id);
        recipeMapper.updateModel(recipe, requestDTO);
        Recipe saved = recipeRepository.save(recipe);

        log.info("Receita atualizada com sucesso. ID: {}", saved.getId());
        return recipeMapper.toResponseDTO(saved);
    }

    public void deleteRecipe(String id) {
        log.info("Removendo receita ID: {}", id);
        findRecipeOrThrow(id);
        recipeRepository.deleteById(id);
        log.info("Receita removida com sucesso. ID: {}", id);
    }

    public List<RecipeSummaryDTO> searchRecipes(String keyword) {
        log.info("Pesquisando receitas com keyword: {}", keyword);
        return recipeRepository.searchByKeyword(keyword)
                .stream()
                .map(recipeMapper::toSummaryDTO)
                .collect(Collectors.toList());
    }

    public List<RecipeSummaryDTO> getRecipesByAuthor(String authorName) {
        log.info("Buscando receitas do autor: {}", authorName);
        return recipeRepository.findByAuthorNameIgnoreCase(authorName)
                .stream()
                .map(recipeMapper::toSummaryDTO)
                .collect(Collectors.toList());
    }

    public List<RecipeSummaryDTO> getRecipesByDifficulty(String difficulty) {
        log.info("Buscando receitas por dificuldade: {}", difficulty);
        validateDifficulty(difficulty);
        return recipeRepository.findByDifficulty(difficulty.toUpperCase())
                .stream()
                .map(recipeMapper::toSummaryDTO)
                .collect(Collectors.toList());
    }

    public List<RecipeSummaryDTO> getRecipesByTags(List<String> tags) {
        log.info("Buscando receitas por tags: {}", tags);
        return recipeRepository.findByTagsIn(tags)
                .stream()
                .map(recipeMapper::toSummaryDTO)
                .collect(Collectors.toList());
    }

    private Recipe findRecipeOrThrow(String id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException(id));
    }

    private void validateDifficulty(String difficulty) {
        if (difficulty != null) {
            List<String> valid = List.of("INICIANTE", "INTERMEDIARIO", "AVANCADO");
            if (!valid.contains(difficulty.toUpperCase())) {
                throw new IllegalArgumentException(
                        "Dificuldade inválida. Use: INICIANTE, INTERMEDIARIO ou AVANCADO");
            }
        }
    }
}
