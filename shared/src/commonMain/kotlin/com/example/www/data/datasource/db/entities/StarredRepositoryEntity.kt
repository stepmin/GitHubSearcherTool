package com.example.www.data.datasource.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "starred_repositories")
data class StarredRepositoryEntity(
    @PrimaryKey val nodeId: String
)