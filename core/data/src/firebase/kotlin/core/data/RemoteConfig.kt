package core.data

import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import utils.Constants
import utils.Constants.REMOTE_CONFIG_TMDB_API_KEY_NAME
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteConfig @Inject constructor() {
    private var isRequesting = false
    private val pendingSuccessListeners = mutableListOf<(String) -> Unit>()
    private val pendingFailureListeners = mutableListOf<() -> Unit>()

    fun fetch(
        onSuccess: (tmdbApiKey: String) -> Unit,
        onFailure: () -> Unit
    ) {
        pendingSuccessListeners.add(onSuccess)
        pendingFailureListeners.add(onFailure)

        if (isRequesting) {
            return
        }
        isRequesting = true

        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 36000
            fetchTimeoutInSeconds = Constants.TIMEOUT_SEC
        }
        remoteConfig.setConfigSettingsAsync(configSettings).addOnCompleteListener {
            remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    pendingSuccessListeners.forEach {
                        it.invoke(remoteConfig.getString(REMOTE_CONFIG_TMDB_API_KEY_NAME))
                    }
                } else {
                    pendingFailureListeners.forEach {
                        it.invoke()
                    }
                }
                pendingSuccessListeners.clear()
                pendingFailureListeners.clear()
                isRequesting = false
            }
        }
    }
}
