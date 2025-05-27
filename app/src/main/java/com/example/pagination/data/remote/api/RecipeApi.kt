package com.example.pagination.data.remote.api

import retrofit2.http.Query
import com.example.pagination.data.remote.dto.RecipeResponseDto
import retrofit2.http.GET

interface RecipeApi {
    @GET("recipes")
    suspend fun getRecipes(
        @Query("limit") limit: Int,
        @Query("skip") skip: Int
    ): RecipeResponseDto

}