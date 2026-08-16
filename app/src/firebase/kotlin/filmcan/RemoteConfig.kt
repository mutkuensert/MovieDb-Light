package filmcan

interface RemoteConfig {

    fun fetchKey(
        key: String,
        onSuccess: (value: String) -> Unit,
        onFailure: () -> Unit
    )

    fun setupUpdateListener(
        configKey: String,
        onUpdated: (value: String) -> Unit,
        onError: (error: Exception) -> Unit
    )
}