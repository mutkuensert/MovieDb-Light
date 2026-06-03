package filmcan

import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.remoteConfig
import timber.log.Timber
import utils.Constants.REMOTE_CONFIG_TMDB_API_KEY_NAME

fun setupRemoteConfigUpdateListener(onUpdated: (apiKey: String) -> Unit) {
    val remoteConfig = Firebase.remoteConfig
    remoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener {
        override fun onUpdate(configUpdate: ConfigUpdate) {
            if (configUpdate.updatedKeys.contains(REMOTE_CONFIG_TMDB_API_KEY_NAME)) {
                remoteConfig.activate().addOnCompleteListener {
                    onUpdated(remoteConfig.getString(REMOTE_CONFIG_TMDB_API_KEY_NAME))
                }
            }
        }

        override fun onError(error: FirebaseRemoteConfigException) {
            Timber.e(error)
        }
    })
}