package com.example.www.ui

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

actual class MyLifecycleScope(private val lifecycleOwner: LifecycleOwner) {
    private val lifecycleScope: LifecycleCoroutineScope = lifecycleOwner.lifecycleScope

    actual fun launchWhenStarted(block: suspend CoroutineScope.() -> Unit) {
        lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                block()
            }
        }
    }
}