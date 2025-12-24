package feature.profile.domain

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.flatMap
import com.github.michaelbull.result.onSuccess
import core.domain.AuthenticationRepository
import core.domain.Failure
import core.domain.User
import core.domain.account.AccountRepository
import core.domain.account.SortBy

class StartSessionUseCase(
    private val accountRepository: AccountRepository,
    private val authenticationRepository: AuthenticationRepository,
) {

    suspend fun execute(): Result<User, Failure> {
        return authenticationRepository.startSession().flatMap {
            accountRepository.fetchAccountDetails().onSuccess {
                accountRepository.fetchWatchlistMovies(SortBy.CreatedAt.ASCENDING)
            }
        }
    }
}