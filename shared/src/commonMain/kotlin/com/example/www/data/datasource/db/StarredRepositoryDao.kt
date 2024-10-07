package com.example.www.data.datasource.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.www.data.datasource.db.entities.StarredRepositoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StarredRepositoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(starredRepositories: List<StarredRepositoryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: StarredRepositoryEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM starred_repositories WHERE nodeId = :nodeId)")
    suspend fun isStarred(nodeId: String): Boolean

    @Query("SELECT * FROM starred_repositories")
    fun getAllAsFlow(): Flow<List<StarredRepositoryEntity>>

    @Query("DELETE FROM starred_repositories WHERE nodeId = :nodeId")
    fun delete(nodeId: String)

    suspend fun starRepository(nodeId: String) {
        insert(StarredRepositoryEntity(nodeId))
    }

    suspend fun unstarRepository(nodeId: String) {
        delete(nodeId)
    }
}