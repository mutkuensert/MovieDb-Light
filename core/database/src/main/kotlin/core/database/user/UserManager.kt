package core.database.user

import android.annotation.SuppressLint
import core.database.encryptedpreferences.EncryptedPreferences
import core.database.encryptedpreferences.EncryptedPreferencesKeys.KEY_USER_DETAILS
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserManager @Inject constructor(
    private val encryptedPreferences: EncryptedPreferences,
    private val json: Json,
) {

    fun getUser(): UserDetails? {
        val userDetailsJson = encryptedPreferences.getString(KEY_USER_DETAILS)
            ?: return null
        return json.decodeFromString<UserDetails>(userDetailsJson)
    }

    @SuppressLint("ApplySharedPref")
    fun setCurrentUser(
        id: Int,
        name: String,
        userName: String,
        profilePicturePath: String?,
        includeAdult: Boolean,
    ): Boolean {
        return encryptedPreferences
            .putString(
                KEY_USER_DETAILS,
                json.encodeToString(
                    UserDetails(id, name, userName, profilePicturePath, includeAdult)
                )
            )
    }

    fun removeCurrentUser(): Boolean {
        return encryptedPreferences.remove(KEY_USER_DETAILS)
    }
}

@Serializable
data class UserDetails(
    val id: Int,
    val name: String,
    val userName: String,
    val profilePicturePath: String?,
    val includeAdult: Boolean,
)
