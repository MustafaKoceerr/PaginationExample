package com.example.pagination.domain.repository

import androidx.paging.PagingData
import com.example.pagination.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    fun getRecipes(): Flow<PagingData<Recipe>>

}