package core.data

import core.database.encryptedpreferences.EncryptedPreferences
import core.database.encryptedpreferences.EncryptedPreferencesKeys.KEY_REQUEST_TOKEN
import core.database.encryptedpreferences.EncryptedPreferencesKeys.KEY_SESSION_ID
import core.domain.auth.AuthStateProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    private val encryptedPreferences: EncryptedPreferences,
) : AuthStateProvider {
    private val _loggedIn = MutableStateFlow(encryptedPreferences.contains(KEY_SESSION_ID))
    override val loggedIn = _loggedIn.asStateFlow()

    fun saveSessionIdSecurely(id: String): Boolean {
        val hasSessionId = encryptedPreferences.putString(KEY_SESSION_ID, id)
        _loggedIn.value = hasSessionId
        return hasSessionId
    }

    fun removeSessionId() {
        val hasNoSessionId = encryptedPreferences.remove(KEY_SESSION_ID)
        _loggedIn.value = !hasNoSessionId
    }

    fun getSessionId(): String? {
        return encryptedPreferences.getString(KEY_SESSION_ID)
    }

    fun setRequestToken(token: String) {
        encryptedPreferences.putString(KEY_REQUEST_TOKEN, token)
    }

    fun getRequestToken(): String? {
        return encryptedPreferences.getString(KEY_REQUEST_TOKEN)
    }

    fun removeRequestToken() {
        encryptedPreferences.remove(KEY_REQUEST_TOKEN)
    }
}
