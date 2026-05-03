package feature.profile.domain.usecase

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.flatMap
import com.github.michaelbull.result.onOk
import core.domain.Failure
import core.domain.account.AccountRepository
import core.domain.account.SortBy
import core.domain.account.User
import core.domain.auth.AuthenticationRepository

class StartSessionUseCase(
    private val accountRepository: AccountRepository,
    private val authenticationRepository: AuthenticationRepository,
) {
    suspend operator fun invoke(): Result<User, Failure> {
        return authenticationRepository.startSession().flatMap {
            accountRepository.getAccountDetails().onOk {
                accountRepository.fetchWatchlistMovies(SortBy.CreatedAt.ASCENDING)
                accountRepository.fetchWatchlistTvShows(SortBy.CreatedAt.ASCENDING)
            }
        }
    }
}