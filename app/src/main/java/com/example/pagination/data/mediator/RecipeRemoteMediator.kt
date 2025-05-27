package com.example.pagination.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import coil.network.HttpException
import com.example.pagination.data.local.database.RecipeDatabase
import com.example.pagination.data.local.entities.RecipeEntity
import com.example.pagination.data.local.entities.RemoteKeys
import com.example.pagination.data.mapper.toEntity
import com.example.pagination.data.remote.api.RecipeApi
import okio.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class RecipeRemoteMediator @Inject constructor(
    private val recipeApi: RecipeApi,
    private val recipeDatabase: RecipeDatabase
) : RemoteMediator<Int, RecipeEntity>() {

    private val recipeDao = recipeDatabase.recipeDao()
    private val remoteKeysDao = recipeDatabase.remoteKeysDao()

    override suspend fun initialize(): InitializeAction {
        // Fresh başlamak istiyorsak LAUNCH_INITIAL_REFRESH
        // Cache'i kullanmak istiyorsak SKIP_INITIAL_REFRESH
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    private suspend fun getRemoteKeyClosesToCurrentPosition(state: PagingState<Int, RecipeEntity>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { recipeId ->
                remoteKeysDao.getRemoteKeysByRecipeId(recipeId)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, RecipeEntity>
    ): RemoteKeys? {
        return state.pages.firstOrNull() {
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let { recipe ->
            remoteKeysDao.getRemoteKeysByRecipeId(recipe.id)
        }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, RecipeEntity>
    ): RemoteKeys? {
        return state.pages.lastOrNull {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { recipe ->
            remoteKeysDao.getRemoteKeysByRecipeId(recipe.id)
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, RecipeEntity>
    ): MediatorResult {

        return try {
            // Hangi sayfaları yükleyeceğimizi belirle
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosesToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: 0
                }

                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevKey = remoteKeys?.prevKey
                    prevKey
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }

                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextKey = remoteKeys?.nextKey
                    nextKey
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
            }

            // API'den veri çek
            val response = recipeApi.getRecipes(
                limit = state.config.pageSize,
                skip = page * state.config.pageSize
            )

            val recipes = response.recipes
            val endOfPaginationReached = recipes!!.isEmpty()

            // Database transaction ile veriyi kaydet
            recipeDatabase.withTransaction {
                // Refresh durumunda eski verileri temizle
                if (loadType == LoadType.REFRESH) {
                    remoteKeysDao.clearRemoteKeys()
                    recipeDao.clearRecipes()
                }

                // Remote keys oluştur
                val prevKey = if (page > 0) page - 1 else null
                val nextKey = if (endOfPaginationReached) null else page + 1

                val remoteKeys = recipes.map { recipe ->
                    RemoteKeys(
                        recipeId = recipe.id!!, // eğer recipe id null gelirse program patlıyor.
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }

                // verileri kaydet
                remoteKeysDao.insertRemoteKeys(remoteKeys)
                recipeDao.insertRecipes(recipes.mapNotNull { it.toEntity() })
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            MediatorResult.Error(exception)
        }
    }

}