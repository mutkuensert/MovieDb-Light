package feature.profile.domain

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onSuccess
import core.domain.auth.AuthStateListener
import core.domain.auth.AuthenticationRepository
import core.domain.Failure

class LogoutUseCase(
    private val authenticationRepository: AuthenticationRepository,
    private val authStateListeners: List<AuthStateListener>,
) {
    suspend operator fun invoke(): Result<Unit, Failure> {
        return authenticationRepository.logout().onSuccess {
            authStateListeners.forEach { it.onUnauthorized() }
        }
    }
}