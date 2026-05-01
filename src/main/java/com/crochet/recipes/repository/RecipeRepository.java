package com.crochet.recipes.repository;

import com.crochet.recipes.model.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends MongoRepository<Recipe, String> {

    Page<Recipe> findByAuthorNameIgnoreCase(String authorName, Pageable pageable);

    List<Recipe> findByAuthorNameIgnoreCase(String authorName);

    @Query("{ 'tags': { $in: ?0 } }")
    Page<Recipe> findByTagsIn(List<String> tags, Pageable pageable);

    @Query("{ 'tags': { $in: ?0 } }")
    List<Recipe> findByTagsIn(List<String> tags);

    @Query("{ $or: [ " +
           "{ 'name': { $regex: ?0, $options: 'i' } }, " +
           "{ 'description': { $regex: ?0, $options: 'i' } }, " +
           "{ 'tags': { $regex: ?0, $options: 'i' } } " +
           "] }")
    Page<Recipe> searchByKeyword(String keyword, Pageable pageable);

    @Query("{ $or: [ " +
           "{ 'name': { $regex: ?0, $options: 'i' } }, " +
           "{ 'description': { $regex: ?0, $options: 'i' } }, " +
           "{ 'tags': { $regex: ?0, $options: 'i' } } " +
           "] }")
    List<Recipe> searchByKeyword(String keyword);

    Page<Recipe> findAllByOrderByCreatedAtDesc(Pageable pageable);

    List<Recipe> findAllByOrderByCreatedAtDesc();
}
