package com.example.pagination.data.remote.dto

import com.google.gson.annotations.SerializedName


data class RecipeDto(
    // KRİTİK ALANLAR - Nullable (olmasa item gösterilemez)
    @SerializedName("id") val id: Long?,
    @SerializedName("name") val name: String?,
    @SerializedName("image") val image: String?,

    // NON-KRİTİK ALANLAR - Nullable ama default değer atanacak
    @SerializedName("ingredients") val ingredients: List<String>?,
    @SerializedName("instructions") val instructions: List<String>?,
    @SerializedName("prepTimeMinutes") val prepTimeMinutes: Long?,
    @SerializedName("cookTimeMinutes") val cookTimeMinutes: Long?,
    @SerializedName("servings") val servings: Long?,
    @SerializedName("difficulty") val difficulty: String?,
    @SerializedName("cuisine") val cuisine: String?,
    @SerializedName("caloriesPerServing") val caloriesPerServing: Long?,
    @SerializedName("tags") val tags: List<String>?,
    @SerializedName("userId") val userId: Long?,
    @SerializedName("rating") val rating: Double?,
    @SerializedName("reviewCount") val reviewCount: Long?,
    @SerializedName("mealType") val mealType: List<String>?
)