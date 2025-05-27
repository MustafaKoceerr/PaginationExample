package com.example.pagination.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.pagination.data.local.database.RecipeDatabase
import com.example.pagination.data.mapper.toDomainModel
import com.example.pagination.data.mediator.RecipeRemoteMediator
import com.example.pagination.data.remote.api.RecipeApi
import com.example.pagination.domain.model.Recipe
import com.example.pagination.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val recipeApi: RecipeApi,
    private val recipeDatabase: RecipeDatabase
) : RecipeRepository {


    @OptIn(ExperimentalPagingApi::class)
    override fun getRecipes(): Flow<PagingData<Recipe>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,           // Her sayfada 20 item
                prefetchDistance = 5,    // 5 item kala yeni sayfa yükle
                enablePlaceholders = false, // Placeholder gösterme
                initialLoadSize = 20     // İlk yüklemede 20 item
            ),
            remoteMediator = RecipeRemoteMediator(
                recipeApi = recipeApi,
                recipeDatabase = recipeDatabase
            ),
            pagingSourceFactory = {
                // Room'dan veri çek (offline first)
                recipeDatabase.recipeDao().getAllRecipes()
            }
        ).flow.map { pagingData ->
            // Entity'leri Domanin model'e çevir
            pagingData.map { recipeEntity ->
                recipeEntity.toDomainModel()
            }
        }
    }
}