package com.example.pagination.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RecipeResponseDto(
    @SerializedName("recipes") val recipes: List<RecipeDto>?,
    @SerializedName("total") val total: Long?,
    @SerializedName("skip") val skip: Long?,
    @SerializedName("limit") val limit: Long?
)