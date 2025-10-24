package core.data

import android.annotation.SuppressLint
import android.content.Context
import androidx.core.content.edit
import core.database.user.EncryptedPreferences
import core.domain.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

private const val PREFS_SESSION: String = "sessionPreferences"
private const val KEY_SESSION_ID: String = "sessionIdKey"
private const val KEY_REQUEST_TOKEN: String = "requestTokenKey"

class SessionManagerImpl(context: Context) :
    SessionManager {
    private val encryptedSharedPreferences = EncryptedPreferences.create(PREFS_SESSION, context)
    private val _loggedIn = MutableStateFlow<Boolean>(
        encryptedSharedPreferences.contains(KEY_SESSION_ID)
    )
    override val loggedIn = _loggedIn.asStateFlow()

    @SuppressLint("ApplySharedPref")
    fun setRequestToken(token: String) {
        encryptedSharedPreferences.edit(commit = true) { putString(KEY_REQUEST_TOKEN, token) }
    }

    fun getRequestToken(): String? {
        return encryptedSharedPreferences.getString(KEY_REQUEST_TOKEN, null)
    }

    @SuppressLint("ApplySharedPref")
    fun removeRequestToken() {
        encryptedSharedPreferences.edit(commit = true) { remove(KEY_REQUEST_TOKEN) }
    }

    fun setSessionId(id: String) {
        val hasSessionId = encryptedSharedPreferences.edit().putString(KEY_SESSION_ID, id).commit()
        _loggedIn.value = hasSessionId
    }

    fun removeSession() {
        val hasNoSessionId = encryptedSharedPreferences.edit().remove(KEY_SESSION_ID).commit()
        _loggedIn.value = !hasNoSessionId
    }

    fun getSessionId(): String? {
        return encryptedSharedPreferences.getString(KEY_SESSION_ID, null)
    }
}