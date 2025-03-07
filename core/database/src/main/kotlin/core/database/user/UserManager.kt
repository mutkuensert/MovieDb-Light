package core.database.user

import android.annotation.SuppressLint
import android.content.Context
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

private const val PREFS_USER = "userPreferences"
private const val KEY_USER_DETAILS = "userDetails"

class UserManager(
    context: Context,
    private val json: Json,
) {
    private val encryptedSharedPreferences = EncryptedPreferences.create(PREFS_USER, context)

    fun getUser(): AccountDetails? {
        val userDetailsJson = encryptedSharedPreferences.getString(KEY_USER_DETAILS, null)
            ?: return null
        return json.decodeFromString<AccountDetails>(userDetailsJson)
    }

    @SuppressLint("ApplySharedPref")
    fun setCurrentUser(
        id: Int,
        name: String,
        userName: String,
        profilePicturePath: String?,
        includeAdult: Boolean,
    ): Boolean {
        return encryptedSharedPreferences.edit()
            .putString(
                KEY_USER_DETAILS,
                json.encodeToString(
                    AccountDetails(id, name, userName, profilePicturePath, includeAdult)
                )
            )
            .commit()
    }

    fun removeCurrentUser(): Boolean {
        return encryptedSharedPreferences.edit().remove(KEY_USER_DETAILS).commit()
    }
}

@Serializable
data class AccountDetails(
    val id: Int,
    val name: String,
    val userName: String,
    val profilePicturePath: String?,
    val includeAdult: Boolean,
)