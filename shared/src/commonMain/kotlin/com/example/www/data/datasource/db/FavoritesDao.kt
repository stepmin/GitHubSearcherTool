package com.example.www.data.datasource.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.www.data.datasource.db.entities.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {
    @Insert
    suspend fun insert(item: FavoriteEntity)

    @Query("SELECT * FROM FavoriteEntity WHERE id = :id")
    suspend fun isFavorite(id: Long): Boolean

    @Query("SELECT * FROM FavoriteEntity")
    fun getAllAsFlow(): Flow<List<FavoriteEntity>>

    @Query("DELETE FROM FavoriteEntity WHERE id = :id")
    suspend fun delete(id: Long)

}