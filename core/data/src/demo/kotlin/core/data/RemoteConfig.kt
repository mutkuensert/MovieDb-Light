package core.data

import filmcan.core.data.BuildConfig

class RemoteConfig {

    @Suppress("UNUSED_PARAMETER")
    fun fetch(
        onSuccess: (tmdbApiKey: String) -> Unit,
        onFailure: () -> Unit
    ) {
        onSuccess.invoke(BuildConfig.API_KEY_TMDB)
    }
}