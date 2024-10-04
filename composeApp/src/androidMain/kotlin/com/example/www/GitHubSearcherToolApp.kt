package com.example.www

import android.app.Application
import com.example.www.di.initKoin
import com.example.www.screens.repositories.RepositoriesViewModel
import org.koin.dsl.module

class GitHubSearcherToolApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(
            listOf(
                module {
                    factory { RepositoriesViewModel(get()) }
                }
            )
        )
    }
}
