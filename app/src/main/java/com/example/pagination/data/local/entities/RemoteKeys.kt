package com.example.pagination.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey
    val recipeId: Long,
    val prevKey: Int?,
    val nextKey: Int?
)