package core.ui

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoadingAnimator @Inject constructor() {
    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    fun start() {
        _loading.value = true
    }

    fun stop() {
        _loading.value = false
    }
}
