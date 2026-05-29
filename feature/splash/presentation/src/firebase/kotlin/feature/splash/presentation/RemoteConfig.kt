package feature.splash.presentation

import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import timber.log.Timber
import utils.Constants

private const val TMDB_API_KEY_NAME = "TMDB_API_KEY"

fun setupRemoteConfig(
    onSuccess: (tmdbApiKey: String) -> Unit,
    onUpdated: (tmdbApiKey: String) -> Unit,
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
                onSuccess.invoke(remoteConfig.getString(TMDB_API_KEY_NAME))
            } else {
                onFailure.invoke()
            }
        }

        remoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener {
            override fun onUpdate(configUpdate: ConfigUpdate) {
                if (configUpdate.updatedKeys.contains(TMDB_API_KEY_NAME)) {
                    remoteConfig.activate().addOnCompleteListener {
                        onUpdated(remoteConfig.getString(TMDB_API_KEY_NAME))
                    }
                }
            }

            override fun onError(error: FirebaseRemoteConfigException) {
                Timber.e(error)
            }
        })
    }
}