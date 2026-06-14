package core.data

import core.domain.ApiKeyManager
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiKeyManagerImpl @Inject constructor() : ApiKeyManager {
    override var tmdbApiKey = MutableStateFlow<String?>(null)
}
