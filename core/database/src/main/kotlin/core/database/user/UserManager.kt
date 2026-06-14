package core.database.user

import android.annotation.SuppressLint
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

private const val PREFS_USER = "userPreferences"
private const val KEY_USER_DETAILS = "userDetails"

@Singleton
class UserManager @Inject constructor(
    @ApplicationContext context: Context,
    private val json: Json,
) {
    private val encryptedSharedPreferences = EncryptedPreferences(context, PREFS_USER)

    fun getUser(): UserDetails? {
        val userDetailsJson = encryptedSharedPreferences.getString(KEY_USER_DETAILS)
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
        return encryptedSharedPreferences
            .putString(
                KEY_USER_DETAILS,
                json.encodeToString(
                    UserDetails(id, name, userName, profilePicturePath, includeAdult)
                )
            )
    }

    fun removeCurrentUser(): Boolean {
        return encryptedSharedPreferences.remove(KEY_USER_DETAILS)
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
