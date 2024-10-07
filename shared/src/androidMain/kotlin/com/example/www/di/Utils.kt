package com.example.www.di

import android.content.Context
import androidx.room.Room
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import com.example.www.data.datasource.db.AppDatabase

fun getMainDatabase(context: Context): AppDatabase {
    val dbFile = context.getDatabasePath("main.db")
    return Room.databaseBuilder(
        context = context.applicationContext,
        klass = AppDatabase::class.java,
        name = dbFile.absolutePath
    )
        .openHelperFactory(FrameworkSQLiteOpenHelperFactory())
        .build()
}