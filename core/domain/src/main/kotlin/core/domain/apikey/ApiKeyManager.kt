package core.domain.apikey

import kotlinx.coroutines.flow.MutableStateFlow

interface ApiKeyManager {
    val tmdbApiKey: MutableStateFlow<ApiKeyState?>
}
