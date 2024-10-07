package com.example.www.data.datasource.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.www.data.datasource.db.entities.StarredRepositoryEntity

@Database(
    entities = [StarredRepositoryEntity::class],
    version = 1
)
abstract class AppDatabase: RoomDatabase() {

    abstract fun starredRepositoryDao(): StarredRepositoryDao

}