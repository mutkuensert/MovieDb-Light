package core.data

import core.domain.ApiKeyManager
import kotlinx.coroutines.flow.MutableStateFlow

class ApiKeyManagerImpl : ApiKeyManager {
    override var tmdbApiKey = MutableStateFlow<String?>(null)
}