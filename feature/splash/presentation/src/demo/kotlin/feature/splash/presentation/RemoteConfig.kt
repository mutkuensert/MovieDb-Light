package feature.splash.presentation

fun setupRemoteConfig(
    onSuccess: (tmdbApiKey: String) -> Unit,
    onUpdated: (tmdbApiKey: String) -> Unit,
    onFailure: () -> Unit
) {
    onSuccess.invoke(BuildConfig.API_KEY_TMDB)
}