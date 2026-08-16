package core.domain.apikey

import kotlinx.coroutines.flow.MutableStateFlow

interface ApiKeyStateHandler {
    val tmdbApiKey: MutableStateFlow<ApiKeyState>
}
