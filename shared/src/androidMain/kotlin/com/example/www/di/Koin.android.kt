package com.example.www.di

import com.example.www.data.datasource.db.AppDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module {
    return module {
        single<AppDatabase> {
            getMainDatabase(get())
        }
    }
}