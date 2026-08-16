package core.data

import core.domain.apikey.ApiKeyStateHandler
import core.domain.apikey.ApiKeyState
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiKeyStateHandlerImpl @Inject constructor() : ApiKeyStateHandler {
    override var tmdbApiKey = MutableStateFlow<ApiKeyState?>(null)
}
