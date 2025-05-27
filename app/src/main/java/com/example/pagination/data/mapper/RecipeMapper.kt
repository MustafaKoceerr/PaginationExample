package com.example.pagination.data.mapper

import com.example.pagination.data.local.entities.RecipeEntity
import com.example.pagination.data.remote.dto.RecipeDto
import com.example.pagination.domain.model.Recipe

// DTO -> Domain Model (null safety ile)
fun RecipeDto.toDomainModel(): Recipe? {
    // Kritik alanları kontrol et
    val safeId = id ?: return null
    val safeName = name ?: return null
    val safeImage = image ?: return null

    return Recipe(
        id = safeId,
        name = safeName,
        image = safeImage,
        // Non-kritik alanlar için default değerler
        ingredients = ingredients ?: emptyList(),
        instructions = instructions ?: emptyList(),
        prepTimeMinutes = prepTimeMinutes ?: 0L,
        cookTimeMinutes = cookTimeMinutes ?: 0L,
        servings = servings ?: 1L,
        difficulty = difficulty ?: "Unknown",
        cuisine = cuisine ?: "Unknown",
        caloriesPerServing = caloriesPerServing ?: 0L,
        tags = tags ?: emptyList(),
        userId = userId ?: 0L,
        rating = rating ?: 0.0,
        reviewCount = reviewCount ?: 0L,
        mealType = mealType ?: emptyList()
    )
}


// DTO -> Entity (Room için)
fun RecipeDto.toEntity(): RecipeEntity? {
    // Kritik alanları kontrol et
    val safeId = id ?: return null
    val safeName = name ?: return null
    val safeImage = image ?: return null

    return RecipeEntity(
        id = safeId,
        name = safeName,
        image = safeImage,
        ingredients = ingredients ?: emptyList(),
        instructions = instructions ?: emptyList(),
        prepTimeMinutes = prepTimeMinutes ?: 0L,
        cookTimeMinutes = cookTimeMinutes ?: 0L,
        servings = servings ?: 1L,
        difficulty = difficulty ?: "Unknown",
        cuisine = cuisine ?: "Unknown",
        caloriesPerServing = caloriesPerServing ?: 0L,
        tags = tags ?: emptyList(),
        userId = userId ?: 0L,
        rating = rating ?: 0.0,
        reviewCount = reviewCount ?: 0L,
        mealType = mealType ?: emptyList()
    )
}



// Entity -> Domain Model
fun RecipeEntity.toDomainModel(): Recipe {
    return Recipe(
        id = id,
        name = name,
        image = image,
        ingredients = ingredients,
        instructions = instructions,
        prepTimeMinutes = prepTimeMinutes,
        cookTimeMinutes = cookTimeMinutes,
        servings = servings,
        difficulty = difficulty,
        cuisine = cuisine,
        caloriesPerServing = caloriesPerServing,
        tags = tags,
        userId = userId,
        rating = rating,
        reviewCount = reviewCount,
        mealType = mealType
    )
}


// List extension functions - null item'ları filtrele
fun List<RecipeDto>.toDomainModelList(): List<Recipe> {
    return mapNotNull { it.toDomainModel() } // null olanları filtrele
}

fun List<RecipeDto>.toEntityList(): List<RecipeEntity> {
    return mapNotNull { it.toEntity() } // null olanları filtrele
}

fun List<RecipeEntity>.entityToDomainModelList(): List<Recipe> {
    return map { it.toDomainModel() }
}