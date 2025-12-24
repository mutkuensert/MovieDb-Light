package core.data

import android.content.Context
import core.database.user.EncryptedPreferences
import core.domain.AuthStateProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

private const val PREFS_SESSION: String = "sessionPreferences"
private const val KEY_SESSION_ID: String = "sessionId"
private const val KEY_REQUEST_TOKEN: String = "requestToken"

class SessionManager(context: Context) : AuthStateProvider {
    private val encryptedSharedPreferences = EncryptedPreferences(context, PREFS_SESSION)
    private val _loggedIn = MutableStateFlow<Boolean>(
        encryptedSharedPreferences.contains(KEY_SESSION_ID)
    )
    override val loggedIn = _loggedIn.asStateFlow()

    fun setSessionId(id: String) {
        val hasSessionId = encryptedSharedPreferences.putString(KEY_SESSION_ID, id)
        _loggedIn.value = hasSessionId
    }

    fun removeSessionId() {
        val hasNoSessionId = encryptedSharedPreferences.remove(KEY_SESSION_ID)
        _loggedIn.value = !hasNoSessionId
    }

    fun requireSessionId(): String {
        return requireNotNull(encryptedSharedPreferences.getString(KEY_SESSION_ID)) {
            "Session id can't be null here."
        }
    }

    fun getSessionId(): String? {
        return encryptedSharedPreferences.getString(KEY_SESSION_ID)
    }

    fun setRequestToken(token: String) {
        encryptedSharedPreferences.putString(KEY_REQUEST_TOKEN, token)
    }

    fun getRequestToken(): String? {
        return encryptedSharedPreferences.getString(KEY_REQUEST_TOKEN)
    }

    fun removeRequestToken() {
        encryptedSharedPreferences.remove(KEY_REQUEST_TOKEN)
    }
}