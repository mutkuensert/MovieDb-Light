package core.data

import filmcan.core.data.BuildConfig

@Suppress("UNUSED_PARAMETER")
fun fetchRemoteConfig(
    onSuccess: (tmdbApiKey: String) -> Unit,
    onFailure: () -> Unit
) {
    onSuccess.invoke(BuildConfig.API_KEY_TMDB)
}