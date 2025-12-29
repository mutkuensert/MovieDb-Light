package feature.profile.domain

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.flatMap
import com.github.michaelbull.result.onSuccess
import core.domain.auth.AuthenticationRepository
import core.domain.Failure
import core.domain.account.AccountRepository
import core.domain.account.SortBy
import core.domain.account.User

class StartSessionUseCase(
    private val accountRepository: AccountRepository,
    private val authenticationRepository: AuthenticationRepository,
) {
    suspend operator fun invoke(): Result<User, Failure> {
        return authenticationRepository.startSession().flatMap {
            accountRepository.fetchAccountDetails().onSuccess {
                accountRepository.fetchWatchlistMovies(SortBy.CreatedAt.ASCENDING)
            }
        }
    }
}