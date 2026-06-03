package core.domain

import kotlinx.coroutines.flow.MutableStateFlow

interface ApiKeyManager {
    val tmdbApiKey: MutableStateFlow<String?>
}