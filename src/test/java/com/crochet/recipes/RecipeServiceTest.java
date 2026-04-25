package com.crochet.recipes;

import com.crochet.recipes.dto.MaterialDTO;
import com.crochet.recipes.dto.RecipePartDTO;
import com.crochet.recipes.dto.RecipeRequestDTO;
import com.crochet.recipes.dto.RecipeResponseDTO;
import com.crochet.recipes.dto.RecipeSummaryDTO;
import com.crochet.recipes.dto.RoundDTO;
import com.crochet.recipes.exception.RecipeNotFoundException;
import com.crochet.recipes.model.Material;
import com.crochet.recipes.model.Recipe;
import com.crochet.recipes.model.RecipePart;
import com.crochet.recipes.model.Round;
import com.crochet.recipes.repository.RecipeRepository;
import com.crochet.recipes.service.RecipeMapper;
import com.crochet.recipes.service.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RecipeService - Testes Unitários Completos")
class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private RecipeMapper recipeMapper;

    @InjectMocks
    private RecipeService recipeService;

    private RecipeRequestDTO requestDTO;
    private Recipe recipe;
    private Recipe recipe2;
    private RecipeResponseDTO responseDTO;
    private RecipeSummaryDTO summaryDTO;

    @BeforeEach
    void setUp() {
        requestDTO = RecipeRequestDTO.builder()
                .name("Urso de Crochê")
                .description("Um urso fofo para presentear")
                .authorName("Maria Silva")
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
                                .rounds(List.of(
                                        RoundDTO.builder()
                                                .roundNumber(1)
                                                .description("Join to first ch with a SC, 15SC(16)")
                                                .build()
                                ))
                                .build()
                ))
                .tags(List.of("urso", "amigurumi", "iniciante"))
                .build();

        recipe = Recipe.builder()
                .id("507f1f77bcf86cd799439011")
                .name("Urso de Crochê")
                .description("Um urso fofo para presentear")
                .authorName("Maria Silva")
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
                                .rounds(List.of(
                                        Round.builder()
                                                .roundNumber(1)
                                                .description("Join to first ch with a SC, 15SC(16)")
                                                .build()
                                ))
                                .build()
                ))
                .tags(List.of("urso", "amigurumi", "iniciante"))
                .createdAt(LocalDateTime.now().minusDays(2))
                .updatedAt(LocalDateTime.now().minusDays(2))
                .build();

        recipe2 = Recipe.builder()
                .id("507f1f77bcf86cd799439012")
                .name("Flor de Crochê")
                .description("Uma flor delicada")
                .authorName("Maria Silva")
                .materials(List.of(
                        Material.builder()
                                .name("Lã Acrílica Rosa")
                                .quantity("50g")
                                .color("Rosa")
                                .build()
                ))
                .parts(new ArrayList<>())
                .tags(List.of("flor", "decoracao"))
                .createdAt(LocalDateTime.now().minusDays(1))
                .updatedAt(LocalDateTime.now().minusDays(1))
                .build();

        responseDTO = RecipeResponseDTO.builder()
                .id("507f1f77bcf86cd799439011")
                .name("Urso de Crochê")
                .authorName("Maria Silva")
                .build();

        summaryDTO = RecipeSummaryDTO.builder()
                .id("507f1f77bcf86cd799439011")
                .name("Urso de Crochê")
                .authorName("Maria Silva")
                .build();
    }

    @Nested
    @DisplayName("Testes de Criação de Receita")
    class CreateRecipeTests {

        @Test
        @DisplayName("Deve criar uma receita com sucesso")
        void shouldCreateRecipeSuccessfully() {
            when(recipeMapper.toModel(requestDTO)).thenReturn(recipe);
            when(recipeRepository.save(recipe)).thenReturn(recipe);
            when(recipeMapper.toResponseDTO(recipe)).thenReturn(responseDTO);

            RecipeResponseDTO result = recipeService.createRecipe(requestDTO);

            assertThat(result)
                    .isNotNull()
                    .satisfies(r -> {
                        assertThat(r.getId()).isEqualTo("507f1f77bcf86cd799439011");
                        assertThat(r.getName()).isEqualTo("Urso de Crochê");
                    });
            verify(recipeRepository, times(1)).save(any());
        }

        @Test
        @DisplayName("Deve criar receita com campos mínimos")
        void shouldCreateRecipeWithMinimalFields() {
            RecipeRequestDTO minimalDTO = RecipeRequestDTO.builder()
                    .name("Receita Mínima")
                    .build();

            Recipe minimalRecipe = Recipe.builder()
                    .id("123")
                    .name("Receita Mínima")
                    .build();

            when(recipeMapper.toModel(minimalDTO)).thenReturn(minimalRecipe);
            when(recipeRepository.save(minimalRecipe)).thenReturn(minimalRecipe);
            when(recipeMapper.toResponseDTO(minimalRecipe)).thenReturn(
                    RecipeResponseDTO.builder().id("123").name("Receita Mínima").build()
            );

            RecipeResponseDTO result = recipeService.createRecipe(minimalDTO);

            assertThat(result).isNotNull();
            verify(recipeRepository).save(any());
        }
    }

    @Nested
    @DisplayName("Testes de Busca por ID")
    class GetRecipeByIdTests {

        @Test
        @DisplayName("Deve retornar receita ao buscar por ID válido")
        void shouldReturnRecipeWhenFoundById() {
            when(recipeRepository.findById(recipe.getId())).thenReturn(Optional.of(recipe));
            when(recipeMapper.toResponseDTO(recipe)).thenReturn(responseDTO);

            RecipeResponseDTO result = recipeService.getRecipeById(recipe.getId());

            assertThat(result)
                    .isNotNull()
                    .satisfies(r -> {
                        assertThat(r.getId()).isEqualTo(recipe.getId());
                        assertThat(r.getName()).isEqualTo("Urso de Crochê");
                    });
            verify(recipeRepository).findById(recipe.getId());
            verify(recipeMapper).toResponseDTO(recipe);
        }

        @Test
        @DisplayName("Deve lançar RecipeNotFoundException quando receita não existe")
        void shouldThrowRecipeNotFoundExceptionWhenRecipeDoesNotExist() {
            String invalidId = "id-invalido";
            when(recipeRepository.findById(invalidId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> recipeService.getRecipeById(invalidId))
                    .isInstanceOf(RecipeNotFoundException.class)
                    .hasMessageContaining(invalidId);
        }

        @Test
        @DisplayName("Deve lançar RecipeNotFoundException com mensagem adequada")
        void shouldThrowRecipeNotFoundExceptionWithProperMessage() {
            String missingId = "999";
            when(recipeRepository.findById(missingId)).thenReturn(Optional.empty());

            RecipeNotFoundException exception = assertThrows(
                    RecipeNotFoundException.class,
                    () -> recipeService.getRecipeById(missingId)
            );

            assertThat(exception.getMessage()).contains(missingId);
        }
    }

    @Nested
    @DisplayName("Testes de Listagem de Todas as Receitas")
    class GetAllRecipesTests {

        @Test
        @DisplayName("Deve retornar lista com múltiplas receitas ordenadas por data de criação")
        void shouldReturnAllRecipesOrderedByCreationDate() {
            List<Recipe> recipes = List.of(recipe2, recipe);

            when(recipeRepository.findAllByOrderByCreatedAtDesc()).thenReturn(recipes);
            when(recipeMapper.toSummaryDTO(any())).thenReturn(summaryDTO);

            List<RecipeSummaryDTO> result = recipeService.getAllRecipes();

            assertThat(result)
                    .isNotNull()
                    .isNotEmpty()
                    .hasSize(2);
            verify(recipeRepository).findAllByOrderByCreatedAtDesc();
            verify(recipeMapper, times(2)).toSummaryDTO(any());
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não há receitas")
        void shouldReturnEmptyListWhenNoRecipesExist() {
            when(recipeRepository.findAllByOrderByCreatedAtDesc()).thenReturn(List.of());

            List<RecipeSummaryDTO> result = recipeService.getAllRecipes();

            assertThat(result).isEmpty();
            verify(recipeRepository).findAllByOrderByCreatedAtDesc();
            verify(recipeMapper, never()).toSummaryDTO(any());
        }

        @Test
        @DisplayName("Deve mapear todos os elementos da lista com sucesso")
        void shouldMapAllRecrusToSummaryDTOSuccessfully() {
            List<Recipe> recipes = List.of(recipe, recipe2);
            RecipeSummaryDTO summary1 = RecipeSummaryDTO.builder().id(recipe.getId()).build();
            RecipeSummaryDTO summary2 = RecipeSummaryDTO.builder().id(recipe2.getId()).build();

            when(recipeRepository.findAllByOrderByCreatedAtDesc()).thenReturn(recipes);
            when(recipeMapper.toSummaryDTO(recipe)).thenReturn(summary1);
            when(recipeMapper.toSummaryDTO(recipe2)).thenReturn(summary2);

            List<RecipeSummaryDTO> result = recipeService.getAllRecipes();

            assertThat(result).hasSize(2);
            assertThat(result.get(0).getId()).isEqualTo(recipe.getId());
            assertThat(result.get(1).getId()).isEqualTo(recipe2.getId());
        }
    }

    @Nested
    @DisplayName("Testes de Atualização de Receita")
    class UpdateRecipeTests {

        @Test
        @DisplayName("Deve atualizar receita com sucesso")
        void shouldUpdateRecipeSuccessfully() {
            String recipeId = recipe.getId();
            RecipeRequestDTO updateDTO = RecipeRequestDTO.builder()
                    .name("Urso Atualizado")
                    .build();

            when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));
            doNothing().when(recipeMapper).updateModel(recipe, updateDTO);
            when(recipeRepository.save(recipe)).thenReturn(recipe);
            when(recipeMapper.toResponseDTO(recipe)).thenReturn(responseDTO);

            RecipeResponseDTO result = recipeService.updateRecipe(recipeId, updateDTO);

            assertThat(result).isNotNull();
            verify(recipeRepository).findById(recipeId);
            verify(recipeMapper).updateModel(recipe, updateDTO);
            verify(recipeRepository).save(recipe);
        }

        @Test
        @DisplayName("Deve lançar exceção ao tentar atualizar receita inexistente")
        void shouldThrowExceptionWhenUpdatingNonexistentRecipe() {
            String invalidId = "999";
            when(recipeRepository.findById(invalidId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> recipeService.updateRecipe(invalidId, requestDTO))
                    .isInstanceOf(RecipeNotFoundException.class);
        }


        @Test
        @DisplayName("Deve atualizar parcialmente uma receita")
        void shouldUpdateRecipePartially() {
            String recipeId = recipe.getId();
            RecipeRequestDTO partialUpdateDTO = RecipeRequestDTO.builder()
                    .name("Novo Nome")
                    .build();

            when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));
            doNothing().when(recipeMapper).updateModel(recipe, partialUpdateDTO);
            when(recipeRepository.save(recipe)).thenReturn(recipe);
            when(recipeMapper.toResponseDTO(recipe)).thenReturn(responseDTO);

            RecipeResponseDTO result = recipeService.updateRecipe(recipeId, partialUpdateDTO);

            assertThat(result).isNotNull();
            verify(recipeRepository).save(recipe);
        }
    }

    @Nested
    @DisplayName("Testes de Deleção de Receita")
    class DeleteRecipeTests {

        @Test
        @DisplayName("Deve deletar receita com sucesso")
        void shouldDeleteRecipeSuccessfully() {
            String recipeId = recipe.getId();
            when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));
            doNothing().when(recipeRepository).deleteById(recipeId);

            assertThatNoException().isThrownBy(() -> recipeService.deleteRecipe(recipeId));

            verify(recipeRepository).findById(recipeId);
            verify(recipeRepository).deleteById(recipeId);
        }

        @Test
        @DisplayName("Deve lançar exceção ao tentar deletar receita inexistente")
        void shouldThrowExceptionWhenDeletingNonexistentRecipe() {
            String invalidId = "999";
            when(recipeRepository.findById(invalidId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> recipeService.deleteRecipe(invalidId))
                    .isInstanceOf(RecipeNotFoundException.class);

            verify(recipeRepository, never()).deleteById(any());
        }

        @Test
        @DisplayName("Deve verificar existência da receita antes de deletar")
        void shouldVerifyRecipeExistenceBeforeDeleting() {
            String recipeId = recipe.getId();
            when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));
            doNothing().when(recipeRepository).deleteById(recipeId);

            recipeService.deleteRecipe(recipeId);

            verify(recipeRepository).findById(recipeId);
            verify(recipeRepository).deleteById(recipeId);
        }
    }

    @Nested
    @DisplayName("Testes de Pesquisa de Receitas")
    class SearchRecipesTests {

        @Test
        @DisplayName("Deve pesquisar receitas por palavra-chave com sucesso")
        void shouldSearchRecipesByKeywordSuccessfully() {
            String keyword = "urso";
            List<Recipe> searchResults = List.of(recipe);

            when(recipeRepository.searchByKeyword(keyword)).thenReturn(searchResults);
            when(recipeMapper.toSummaryDTO(recipe)).thenReturn(summaryDTO);

            List<RecipeSummaryDTO> result = recipeService.searchRecipes(keyword);

            assertThat(result).isNotEmpty().hasSize(1);
            verify(recipeRepository).searchByKeyword(keyword);
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando nenhuma receita corresponde à pesquisa")
        void shouldReturnEmptyListWhenNoMatchesFound() {
            String keyword = "inexistente";
            when(recipeRepository.searchByKeyword(keyword)).thenReturn(List.of());

            List<RecipeSummaryDTO> result = recipeService.searchRecipes(keyword);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Deve pesquisar receitas com múltiplas correspondências")
        void shouldSearchRecipesWithMultipleMatches() {
            String keyword = "crochê";
            List<Recipe> searchResults = List.of(recipe, recipe2);

            when(recipeRepository.searchByKeyword(keyword)).thenReturn(searchResults);
            when(recipeMapper.toSummaryDTO(any())).thenReturn(summaryDTO);

            List<RecipeSummaryDTO> result = recipeService.searchRecipes(keyword);

            assertThat(result).hasSize(2);
            verify(recipeRepository).searchByKeyword(keyword);
            verify(recipeMapper, times(2)).toSummaryDTO(any());
        }

        @Test
        @DisplayName("Deve pesquisar com palavra-chave vazia")
        void shouldSearchWithEmptyKeyword() {
            when(recipeRepository.searchByKeyword("")).thenReturn(List.of());

            List<RecipeSummaryDTO> result = recipeService.searchRecipes("");

            assertThat(result).isEmpty();
            verify(recipeRepository).searchByKeyword("");
        }
    }

    @Nested
    @DisplayName("Testes de Busca por Autor")
    class GetRecipesByAuthorTests {

        @Test
        @DisplayName("Deve retornar receitas do autor com sucesso")
        void shouldReturnRecipesByAuthorSuccessfully() {
            String author = "Maria Silva";
            List<Recipe> authorRecipes = List.of(recipe, recipe2);

            when(recipeRepository.findByAuthorNameIgnoreCase(author)).thenReturn(authorRecipes);
            when(recipeMapper.toSummaryDTO(any())).thenReturn(summaryDTO);

            List<RecipeSummaryDTO> result = recipeService.getRecipesByAuthor(author);

            assertThat(result).hasSize(2);
            verify(recipeRepository).findByAuthorNameIgnoreCase(author);
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando autor não possui receitas")
        void shouldReturnEmptyListWhenAuthorHasNoRecipes() {
            String author = "Autor Inexistente";
            when(recipeRepository.findByAuthorNameIgnoreCase(author)).thenReturn(List.of());

            List<RecipeSummaryDTO> result = recipeService.getRecipesByAuthor(author);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Deve buscar autor de forma case-insensitive")
        void shouldSearchAuthorCaseInsensitive() {
            String author = "maria silva";
            when(recipeRepository.findByAuthorNameIgnoreCase(author)).thenReturn(List.of(recipe));
            when(recipeMapper.toSummaryDTO(recipe)).thenReturn(summaryDTO);

            List<RecipeSummaryDTO> result = recipeService.getRecipesByAuthor(author);

            assertThat(result).hasSize(1);
            verify(recipeRepository).findByAuthorNameIgnoreCase(author);
        }

        @Test
        @DisplayName("Deve retornar uma receita quando autor tem apenas uma")
        void shouldReturnSingleRecipeWhenAuthorHasOne() {
            String author = "Maria Silva";
            when(recipeRepository.findByAuthorNameIgnoreCase(author)).thenReturn(List.of(recipe));
            when(recipeMapper.toSummaryDTO(recipe)).thenReturn(summaryDTO);

            List<RecipeSummaryDTO> result = recipeService.getRecipesByAuthor(author);

            assertThat(result).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Testes de Busca por Tags")
    class GetRecipesByTagsTests {

        @Test
        @DisplayName("Deve retornar receitas que contêm as tags especificadas")
        void shouldReturnRecipesWithGivenTags() {
            List<String> tags = List.of("urso", "amigurumi");
            List<Recipe> recipesWithTags = List.of(recipe);

            when(recipeRepository.findByTagsIn(tags)).thenReturn(recipesWithTags);
            when(recipeMapper.toSummaryDTO(recipe)).thenReturn(summaryDTO);

            List<RecipeSummaryDTO> result = recipeService.getRecipesByTags(tags);

            assertThat(result).hasSize(1);
            verify(recipeRepository).findByTagsIn(tags);
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando nenhuma receita possui as tags")
        void shouldReturnEmptyListWhenNoRecipesHaveTags() {
            List<String> tags = List.of("inexistente");
            when(recipeRepository.findByTagsIn(tags)).thenReturn(List.of());

            List<RecipeSummaryDTO> result = recipeService.getRecipesByTags(tags);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Deve retornar múltiplas receitas para múltiplas tags")
        void shouldReturnMultipleRecipesForMultipleTags() {
            List<String> tags = List.of("crochê", "decoracao");
            List<Recipe> recipes = List.of(recipe, recipe2);

            when(recipeRepository.findByTagsIn(tags)).thenReturn(recipes);
            when(recipeMapper.toSummaryDTO(any())).thenReturn(summaryDTO);

            List<RecipeSummaryDTO> result = recipeService.getRecipesByTags(tags);

            assertThat(result).hasSize(2);
            verify(recipeRepository).findByTagsIn(tags);
        }

        @Test
        @DisplayName("Deve buscar com lista vazia de tags")
        void shouldSearchWithEmptyTagsList() {
            when(recipeRepository.findByTagsIn(List.of())).thenReturn(List.of());

            List<RecipeSummaryDTO> result = recipeService.getRecipesByTags(List.of());

            assertThat(result).isEmpty();
            verify(recipeRepository).findByTagsIn(List.of());
        }

        @Test
        @DisplayName("Deve buscar com uma única tag")
        void shouldSearchWithSingleTag() {
            List<String> tags = List.of("urso");
            when(recipeRepository.findByTagsIn(tags)).thenReturn(List.of(recipe));
            when(recipeMapper.toSummaryDTO(recipe)).thenReturn(summaryDTO);

            List<RecipeSummaryDTO> result = recipeService.getRecipesByTags(tags);

            assertThat(result).hasSize(1);
        }
    }
}