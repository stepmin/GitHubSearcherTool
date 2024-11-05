package com.example.www.ui

import kotlinx.coroutines.CoroutineScope

expect class MyLifecycleScope {
    fun launchWhenStarted(block: suspend CoroutineScope.() -> Unit)
}