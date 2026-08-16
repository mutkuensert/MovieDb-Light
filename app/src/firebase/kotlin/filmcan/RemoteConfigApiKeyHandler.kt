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
        if (apiKeyStateHandler.tmdbApiKey.value == null) {
            remoteConfig.fetchKey(
                REMOTE_CONFIG_TMDB_API_KEY_NAME,
                onSuccess = { apiKey ->
                    apiKeyStateHandler.tmdbApiKey.value = ApiKeyState.Success(apiKey)
                },
                onFailure = { apiKeyStateHandler.tmdbApiKey.value = ApiKeyState.Failed() }
            )
        }

        remoteConfig.setupUpdateListener(
            REMOTE_CONFIG_TMDB_API_KEY_NAME,
            onUpdated = { apiKey: String ->
                apiKeyStateHandler.tmdbApiKey.value = ApiKeyState.Success(apiKey)
            },
            onError = { _ -> })
    }
}
