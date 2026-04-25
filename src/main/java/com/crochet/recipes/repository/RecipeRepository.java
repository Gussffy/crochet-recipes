package com.crochet.recipes.repository;

import com.crochet.recipes.model.Recipe;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends MongoRepository<Recipe, String> {

    List<Recipe> findByAuthorNameIgnoreCase(String authorName);

    @Query("{ 'tags': { $in: ?0 } }")
    List<Recipe> findByTagsIn(List<String> tags);

    @Query("{ $or: [ " +
           "{ 'name': { $regex: ?0, $options: 'i' } }, " +
           "{ 'description': { $regex: ?0, $options: 'i' } }, " +
           "{ 'tags': { $regex: ?0, $options: 'i' } } " +
           "] }")
    List<Recipe> searchByKeyword(String keyword);

    List<Recipe> findAllByOrderByCreatedAtDesc();
}
