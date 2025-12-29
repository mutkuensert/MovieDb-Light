package core.domain.auth

interface AuthStateListener {
    suspend fun onUnauthorized()
}