package filmcan

import com.mutkuensert.filmcan.BuildConfig
import core.domain.apikey.ApiKeyState
import core.domain.apikey.ApiKeyStateHandler
import javax.inject.Inject

class RemoteConfigApiKeyHandler @Inject constructor(
    private val apiKeyStateHandler: ApiKeyStateHandler,
) {

    fun initialize() {
        apiKeyStateHandler.tmdbApiKey.value = ApiKeyState.Success(BuildConfig.API_KEY_TMDB)
    }

    fun setupRemoteConfigUpdateListener(onUpdated: (apiKey: String) -> Unit) {}
}
