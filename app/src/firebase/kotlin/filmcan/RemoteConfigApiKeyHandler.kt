package filmcan

import core.domain.apikey.ApiKeyState
import core.domain.apikey.ApiKeyStateHandler
import utils.Constants.REMOTE_CONFIG_TMDB_API_KEY_NAME
import javax.inject.Inject

class RemoteConfigApiKeyHandler @Inject constructor(
    private val apiKeyStateHandler: ApiKeyStateHandler,
    private val remoteConfig: RemoteConfig,
) {

    fun initialize() {
        remoteConfig.setupUpdateListener(
            REMOTE_CONFIG_TMDB_API_KEY_NAME,
            onUpdated = { apiKey: String ->
                apiKeyStateHandler.tmdbApiKey.value = ApiKeyState.Success(apiKey)
            },
            onError = { _ -> })
    }

    fun fetchApiKeyIfNotFetched() {
        if (apiKeyStateHandler.tmdbApiKey.value is ApiKeyState.NotRequested ||
            apiKeyStateHandler.tmdbApiKey.value is ApiKeyState.Failed
        ) {
            remoteConfig.fetchKey(
                REMOTE_CONFIG_TMDB_API_KEY_NAME,
                onSuccess = { apiKey ->
                    apiKeyStateHandler.tmdbApiKey.value = ApiKeyState.Success(apiKey)
                },
                onFailure = { apiKeyStateHandler.tmdbApiKey.value = ApiKeyState.Failed() }
            )
        }
    }
}
