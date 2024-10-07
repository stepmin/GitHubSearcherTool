package com.example.www

import android.app.Application
import com.example.www.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class GitHubSearcherToolApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidLogger()
            androidContext(this@GitHubSearcherToolApp)
        }
    }
}
