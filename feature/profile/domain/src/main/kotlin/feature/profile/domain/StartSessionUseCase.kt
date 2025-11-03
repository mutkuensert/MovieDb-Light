package feature.profile.domain

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.flatMap
import com.github.michaelbull.result.map
import core.domain.AccountRepository
import core.domain.AuthenticationRepository
import core.domain.ErrorMessage
import core.domain.User

class StartSessionUseCase(
    private val accountRepository: AccountRepository,
    private val authenticationRepository: AuthenticationRepository,
) {

    suspend fun execute(): Result<User, ErrorMessage> {
        return authenticationRepository.startSession().flatMap {
            accountRepository.fetchAccountDetails().map { user ->
                accountRepository.fetchFavoriteMovies()
                user
            }
        }
    }
}