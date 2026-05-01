package com.crochet.recipes.service;

import com.crochet.recipes.dto.request.RecipeRequestDTO;
import com.crochet.recipes.dto.response.RecipeResponseDTO;
import com.crochet.recipes.dto.response.RecipeSummaryDTO;
import com.crochet.recipes.exception.RecipeNotFoundException;
import com.crochet.recipes.mapper.RecipeRequestMapper;
import com.crochet.recipes.mapper.RecipeResponseMapper;
import com.crochet.recipes.model.Recipe;
import com.crochet.recipes.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeRequestMapper recipeRequestMapper;
    private final RecipeResponseMapper recipeResponseMapper;

    public RecipeResponseDTO createRecipe(RecipeRequestDTO requestDTO) {
        log.info("Criando nova receita: {}", requestDTO.name());

        Recipe recipe = recipeRequestMapper.toModel(requestDTO);
        Recipe saved = recipeRepository.save(recipe);

        log.info("Receita criada com sucesso. ID: {}", saved.getId());
        return recipeResponseMapper.toResponseDTO(saved);
    }

    public RecipeResponseDTO getRecipeById(String id) {
        log.info("Buscando receita por ID: {}", id);
        Recipe recipe = findRecipeOrThrow(id);
        return recipeResponseMapper.toResponseDTO(recipe);
    }

    public List<RecipeSummaryDTO> getAllRecipes() {
        log.info("Listando todas as receitas");
        return recipeRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(recipeResponseMapper::toSummaryDTO)
                .collect(Collectors.toList());
    }

    public Page<RecipeSummaryDTO> getAllRecipesPaginated(Pageable pageable) {
        log.info("Listando receitas com paginação: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        return recipeRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(recipeResponseMapper::toSummaryDTO);
    }

    public RecipeResponseDTO updateRecipe(String id, RecipeRequestDTO requestDTO) {
        log.info("Atualizando receita ID: {}", id);

        Recipe recipe = findRecipeOrThrow(id);
        recipeRequestMapper.updateModel(recipe, requestDTO);
        Recipe saved = recipeRepository.save(recipe);

        log.info("Receita atualizada com sucesso. ID: {}", saved.getId());
        return recipeResponseMapper.toResponseDTO(saved);
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
                .map(recipeResponseMapper::toSummaryDTO)
                .collect(Collectors.toList());
    }

    public Page<RecipeSummaryDTO> searchRecipesPaginated(String keyword, Pageable pageable) {
        log.info("Pesquisando receitas com keyword: {} (paginado)", keyword);
        return recipeRepository.searchByKeyword(keyword, pageable)
                .map(recipeResponseMapper::toSummaryDTO);
    }

    public List<RecipeSummaryDTO> getRecipesByAuthor(String authorName) {
        log.info("Buscando receitas do autor: {}", authorName);
        return recipeRepository.findByAuthorNameIgnoreCase(authorName)
                .stream()
                .map(recipeResponseMapper::toSummaryDTO)
                .collect(Collectors.toList());
    }

    public Page<RecipeSummaryDTO> getRecipesByAuthorPaginated(String authorName, Pageable pageable) {
        log.info("Buscando receitas do autor: {} (paginado)", authorName);
        return recipeRepository.findByAuthorNameIgnoreCase(authorName, pageable)
                .map(recipeResponseMapper::toSummaryDTO);
    }

    public List<RecipeSummaryDTO> getRecipesByTags(List<String> tags) {
        log.info("Buscando receitas por tags: {}", tags);
        return recipeRepository.findByTagsIn(tags)
                .stream()
                .map(recipeResponseMapper::toSummaryDTO)
                .collect(Collectors.toList());
    }

    public Page<RecipeSummaryDTO> getRecipesByTagsPaginated(List<String> tags, Pageable pageable) {
        log.info("Buscando receitas por tags: {} (paginado)", tags);
        return recipeRepository.findByTagsIn(tags, pageable)
                .map(recipeResponseMapper::toSummaryDTO);
    }

    private Recipe findRecipeOrThrow(String id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException(id));
    }
}