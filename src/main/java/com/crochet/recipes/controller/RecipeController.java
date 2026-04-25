package com.crochet.recipes.controller;

import com.crochet.recipes.dto.ApiResponseDTO;
import com.crochet.recipes.dto.RecipeRequestDTO;
import com.crochet.recipes.dto.RecipeResponseDTO;
import com.crochet.recipes.dto.RecipeSummaryDTO;
import com.crochet.recipes.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/recipes")
@RequiredArgsConstructor
@Tag(name = "Receitas de Crochê", description = "API para gerenciamento de receitas de crochê")
public class RecipeController {

    private final RecipeService recipeService;

    @PostMapping
    @Operation(summary = "Criar nova receita", description = "Publica uma nova receita de crochê com partes e imagens")
    public ResponseEntity<ApiResponseDTO<RecipeResponseDTO>> createRecipe(
            @Valid @RequestBody RecipeRequestDTO requestDTO) {

        RecipeResponseDTO created = recipeService.createRecipe(requestDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success(created, "Receita criada com sucesso!"));
    }

    @GetMapping
    @Operation(summary = "Listar todas as receitas", description = "Retorna um resumo de todas as receitas ordenadas por data de criação")
    public ResponseEntity<ApiResponseDTO<List<RecipeSummaryDTO>>> getAllRecipes() {
        List<RecipeSummaryDTO> recipes = recipeService.getAllRecipes();
        return ResponseEntity.ok(ApiResponseDTO.success(recipes,
                recipes.size() + " receita(s) encontrada(s)"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar receita por ID", description = "Retorna todos os detalhes de uma receita incluindo partes e imagens")
    public ResponseEntity<ApiResponseDTO<RecipeResponseDTO>> getRecipeById(
            @Parameter(description = "ID da receita") @PathVariable String id) {

        RecipeResponseDTO recipe = recipeService.getRecipeById(id);
        return ResponseEntity.ok(ApiResponseDTO.success(recipe));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar receita", description = "Atualiza todos os dados de uma receita existente")
    public ResponseEntity<ApiResponseDTO<RecipeResponseDTO>> updateRecipe(
            @Parameter(description = "ID da receita") @PathVariable String id,
            @Valid @RequestBody RecipeRequestDTO requestDTO) {

        RecipeResponseDTO updated = recipeService.updateRecipe(id, requestDTO);
        return ResponseEntity.ok(ApiResponseDTO.success(updated, "Receita atualizada com sucesso!"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar receita", description = "Remove uma receita permanentemente")
    public ResponseEntity<ApiResponseDTO<Void>> deleteRecipe(
            @Parameter(description = "ID da receita") @PathVariable String id) {

        recipeService.deleteRecipe(id);
        return ResponseEntity.ok(ApiResponseDTO.success(null, "Receita removida com sucesso!"));
    }

    @GetMapping("/search")
    @Operation(summary = "Pesquisar receitas", description = "Pesquisa receitas por nome, descrição ou tags")
    public ResponseEntity<ApiResponseDTO<List<RecipeSummaryDTO>>> searchRecipes(
            @Parameter(description = "Palavra-chave para pesquisa") @RequestParam String keyword) {

        List<RecipeSummaryDTO> recipes = recipeService.searchRecipes(keyword);
        return ResponseEntity.ok(ApiResponseDTO.success(recipes,
                recipes.size() + " receita(s) encontrada(s) para: " + keyword));
    }

    @GetMapping("/author/{authorName}")
    @Operation(summary = "Buscar receitas por autor")
    public ResponseEntity<ApiResponseDTO<List<RecipeSummaryDTO>>> getByAuthor(
            @Parameter(description = "Nome do autor") @PathVariable String authorName) {

        List<RecipeSummaryDTO> recipes = recipeService.getRecipesByAuthor(authorName);
        return ResponseEntity.ok(ApiResponseDTO.success(recipes,
                recipes.size() + " receita(s) de " + authorName));
    }


    @GetMapping("/tags")
    @Operation(summary = "Buscar receitas por tags")
    public ResponseEntity<ApiResponseDTO<List<RecipeSummaryDTO>>> getByTags(
            @Parameter(description = "Lista de tags") @RequestParam List<String> tags) {

        List<RecipeSummaryDTO> recipes = recipeService.getRecipesByTags(tags);
        return ResponseEntity.ok(ApiResponseDTO.success(recipes,
                recipes.size() + " receita(s) encontrada(s)"));
    }
}
