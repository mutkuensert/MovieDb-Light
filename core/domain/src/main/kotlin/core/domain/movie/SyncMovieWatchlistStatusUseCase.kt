package core.domain.movie

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onSuccess
import core.domain.Failure
import core.domain.account.AccountRepository
import core.domain.profile.MovieWatchlistChangeListener

class SyncMovieWatchlistStatusUseCase(
    private val movieWatchlistChangeListener: MovieWatchlistChangeListener,
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(movieId: Int, inWatchlist: Boolean): Result<Unit, Failure> {
        return accountRepository.syncMovieWatchlistStatus(movieId, inWatchlist).onSuccess {
            movieWatchlistChangeListener.onMovieWatchlistChanged()
        }
    }
}