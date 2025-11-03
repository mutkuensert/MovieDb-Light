package core.domain

interface AuthStateListener {
    suspend fun onUnauthorized()
}