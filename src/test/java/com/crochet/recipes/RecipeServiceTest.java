package com.crochet.recipes;

import com.crochet.recipes.dto.MaterialDTO;
import com.crochet.recipes.dto.RecipePartDTO;
import com.crochet.recipes.dto.RecipeRequestDTO;
import com.crochet.recipes.dto.RecipeResponseDTO;
import com.crochet.recipes.exception.RecipeNotFoundException;
import com.crochet.recipes.model.Material;
import com.crochet.recipes.model.Recipe;
import com.crochet.recipes.model.RecipePart;
import com.crochet.recipes.repository.RecipeRepository;
import com.crochet.recipes.service.RecipeMapper;
import com.crochet.recipes.service.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RecipeService - Testes Unitários")
class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private RecipeMapper recipeMapper;

    @InjectMocks
    private RecipeService recipeService;

    private RecipeRequestDTO requestDTO;
    private Recipe recipe;
    private RecipeResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        requestDTO = RecipeRequestDTO.builder()
                .name("Urso de Crochê")
                .description("Um urso fofo para presentear")
                .authorName("Maria Silva")
                .difficulty("INICIANTE")
                .materials(List.of(
                        MaterialDTO.builder()
                                .name("Lã Acrílica Bege")
                                .quantity("100g")
                                .color("Bege")
                                .build()
                ))
                .parts(List.of(
                        RecipePartDTO.builder()
                                .order(1)
                                .title("Parte 1 - Cabeça do Urso")
                                .instructions("Faça um anel mágico com 6 pontos...")
                                .build()
                ))
                .tags(List.of("urso", "amigurumi", "iniciante"))
                .build();

        recipe = Recipe.builder()
                .id("507f1f77bcf86cd799439011")
                .name("Urso de Crochê")
                .description("Um urso fofo para presentear")
                .authorName("Maria Silva")
                .difficulty("INICIANTE")
                .materials(List.of(
                        Material.builder()
                                .name("Lã Acrílica Bege")
                                .quantity("100g")
                                .color("Bege")
                                .build()
                ))
                .parts(List.of(
                        RecipePart.builder()
                                .order(1)
                                .title("Parte 1 - Cabeça do Urso")
                                .instructions("Faça um anel mágico com 6 pontos...")
                                .build()
                ))
                .tags(List.of("urso", "amigurumi", "iniciante"))
                .createdAt(LocalDateTime.now())
                .build();

        responseDTO = RecipeResponseDTO.builder()
                .id("507f1f77bcf86cd799439011")
                .name("Urso de Crochê")
                .authorName("Maria Silva")
                .difficulty("INICIANTE")
                .build();
    }

    @Test
    @DisplayName("Deve criar uma receita com sucesso")
    void shouldCreateRecipeSuccessfully() {
        when(recipeMapper.toModel(requestDTO)).thenReturn(recipe);
        when(recipeRepository.save(recipe)).thenReturn(recipe);
        when(recipeMapper.toResponseDTO(recipe)).thenReturn(responseDTO);

        RecipeResponseDTO result = recipeService.createRecipe(requestDTO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("507f1f77bcf86cd799439011");
        verify(recipeRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar receita inexistente")
    void shouldThrowExceptionWhenRecipeNotFound() {
        when(recipeRepository.findById("id-invalido")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> recipeService.getRecipeById("id-invalido"))
                .isInstanceOf(RecipeNotFoundException.class)
                .hasMessageContaining("id-invalido");
    }

    @Test
    @DisplayName("Deve retornar receita ao buscar por ID válido")
    void shouldReturnRecipeWhenFoundById() {
        when(recipeRepository.findById(recipe.getId())).thenReturn(Optional.of(recipe));
        when(recipeMapper.toResponseDTO(recipe)).thenReturn(responseDTO);

        RecipeResponseDTO result = recipeService.getRecipeById(recipe.getId());

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Urso de Crochê");
    }

    @Test
    @DisplayName("Deve lançar exceção com dificuldade inválida")
    void shouldThrowExceptionWithInvalidDifficulty() {
        requestDTO.setDifficulty("EXPERT");

        assertThatThrownBy(() -> recipeService.createRecipe(requestDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Dificuldade inválida");
    }

    @Test
    @DisplayName("Deve deletar receita com sucesso")
    void shouldDeleteRecipeSuccessfully() {
        when(recipeRepository.findById(recipe.getId())).thenReturn(Optional.of(recipe));
        doNothing().when(recipeRepository).deleteById(recipe.getId());

        recipeService.deleteRecipe(recipe.getId());

        verify(recipeRepository, times(1)).deleteById(recipe.getId());
    }
}
