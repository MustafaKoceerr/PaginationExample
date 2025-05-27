package com.example.pagination.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pagination.data.local.dao.RecipeDao
import com.example.pagination.data.local.dao.RemoteKeysDao
import com.example.pagination.data.local.entities.RecipeEntity
import com.example.pagination.data.local.entities.RemoteKeys

@Database(
    entities = [RecipeEntity::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class RecipeDatabase() : RoomDatabase() {

    abstract fun recipeDao(): RecipeDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}