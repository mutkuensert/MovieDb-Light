package filmcan

import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import timber.log.Timber
import utils.Constants
import javax.inject.Inject

class RemoteConfigImpl @Inject constructor() : RemoteConfig {

    override fun fetchKey(
        key: String,
        onSuccess: (value: String) -> Unit,
        onFailure: () -> Unit,
    ) {
        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 36000
            fetchTimeoutInSeconds = Constants.TIMEOUT_SEC
        }
        remoteConfig.setConfigSettingsAsync(configSettings).addOnCompleteListener {
            remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess(remoteConfig.getString(key))
                } else {
                    onFailure()
                }
            }
        }
    }

    /**
     * @param configKey A Firebase Remote Config parameter key.
     */
    override fun setupUpdateListener(
        configKey: String,
        onUpdated: (value: String) -> Unit,
        onError: (error: Exception) -> Unit
    ) {
        val remoteConfig = Firebase.remoteConfig
        remoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener {
            override fun onUpdate(configUpdate: ConfigUpdate) {
                if (configUpdate.updatedKeys.contains(configKey)) {
                    remoteConfig.activate().addOnCompleteListener {
                        onUpdated.invoke(remoteConfig.getString(configKey))
                    }
                }
            }

            override fun onError(error: FirebaseRemoteConfigException) {
                Timber.e(error)
                onError.invoke(error)
            }
        })
    }
}