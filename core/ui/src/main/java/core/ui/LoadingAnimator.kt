package core.ui

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoadingAnimator {
    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    fun start() {
        _loading.value = true
    }

    fun stop() {
        _loading.value = false
    }
}