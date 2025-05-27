package com.example.pagination.presentation.recipe

import androidx.paging.PagingData
import com.example.pagination.domain.model.Recipe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class RecipeUiState(
    val recipes: Flow<PagingData<Recipe>> = emptyFlow(),
    val isLoading: Boolean = false,
    val error: String? = null
)