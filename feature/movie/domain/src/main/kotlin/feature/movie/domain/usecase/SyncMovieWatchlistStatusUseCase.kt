package feature.movie.domain.usecase

import javax.inject.Inject

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onOk
import core.domain.Failure
import core.domain.account.AccountRepository
import core.domain.profile.WatchlistMoviesRefresher

class SyncMovieWatchlistStatusUseCase @Inject constructor(
    private val watchlistMoviesRefresher: WatchlistMoviesRefresher,
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(movieId: Int, inWatchlist: Boolean): Result<Unit, Failure> {
        return accountRepository.syncMovieWatchlistStatus(movieId, inWatchlist).onOk {
            watchlistMoviesRefresher()
        }
    }
}