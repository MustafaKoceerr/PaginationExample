package com.example.pagination.di

import android.content.Context
import androidx.room.Room
import com.example.pagination.core.constants.DatabaseConstants
import com.example.pagination.data.local.dao.RecipeDao
import com.example.pagination.data.local.dao.RemoteKeysDao
import com.example.pagination.data.local.database.RecipeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideRecipeDatabase(
        @ApplicationContext context: Context
    ): RecipeDatabase {
        return Room.databaseBuilder(
            context,
            RecipeDatabase::class.java,
            DatabaseConstants.DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideRecipeDao(database: RecipeDatabase): RecipeDao {
        return database.recipeDao()
    }

    @Provides
    fun provideRemoteKeysDao(database: RecipeDatabase): RemoteKeysDao {
        return database.remoteKeysDao()
    }

}