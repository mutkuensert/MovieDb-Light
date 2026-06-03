package core.data

import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import utils.Constants
import utils.Constants.REMOTE_CONFIG_TMDB_API_KEY_NAME

fun fetchRemoteConfig(
    onSuccess: (tmdbApiKey: String) -> Unit,
    onFailure: () -> Unit
) {
    val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
    val configSettings = remoteConfigSettings {
        minimumFetchIntervalInSeconds = 36000
        fetchTimeoutInSeconds = Constants.TIMEOUT_SEC
    }
    remoteConfig.setConfigSettingsAsync(configSettings).addOnCompleteListener {
        remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess.invoke(remoteConfig.getString(REMOTE_CONFIG_TMDB_API_KEY_NAME))
            } else {
                onFailure.invoke()
            }
        }
    }
}
