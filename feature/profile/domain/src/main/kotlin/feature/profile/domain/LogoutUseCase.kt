package feature.profile.domain

import com.github.michaelbull.result.onSuccess
import core.domain.AuthStateListener
import core.domain.AuthenticationRepository

class LogoutUseCase(
    private val authenticationRepository: AuthenticationRepository,
    private val authStateListeners: List<AuthStateListener>,
) {

    suspend fun execute() {
        authenticationRepository.logout().onSuccess {
            authStateListeners.forEach { it.onUnauthorized() }
        }
    }
}