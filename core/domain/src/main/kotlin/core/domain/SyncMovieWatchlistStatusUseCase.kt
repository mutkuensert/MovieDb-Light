package core.domain

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onSuccess
import core.domain.profile.ProfileWatchlistMoviesPagingInvalidator

class SyncMovieWatchlistStatusUseCase(
    private val profileWatchlistMoviesPagingInvalidator: ProfileWatchlistMoviesPagingInvalidator,
    private val accountRepository: AccountRepository
) {
    suspend fun execute(movieId: Int, inWatchlist: Boolean): Result<Unit, ErrorMessage> {
        return accountRepository.syncMovieWatchlistStatus(movieId, inWatchlist).onSuccess {
            profileWatchlistMoviesPagingInvalidator.invalidateWatchlistMovies()
        }
    }
}