package com.example.www.ui

import kotlinx.coroutines.CoroutineScope

actual class MyLifecycleScope {
    actual fun launchWhenStarted(block: suspend CoroutineScope.() -> Unit) {
    }
}