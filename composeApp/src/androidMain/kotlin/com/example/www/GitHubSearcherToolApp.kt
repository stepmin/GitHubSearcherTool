package com.example.www

import android.app.Application
import com.example.www.di.initKoin

class GitHubSearcherToolApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }
}
