package feature.tvshow.domain.usecase

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onSuccess
import core.domain.Failure
import core.domain.account.AccountRepository
import core.domain.profile.WatchlistTvShowsRefresher

class SyncTvShowWatchlistStatusUseCase(
    private val watchlistTvShowsRefresher: WatchlistTvShowsRefresher,
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(tvShowId: Int, inWatchlist: Boolean): Result<Unit, Failure> {
        return accountRepository.syncTvShowWatchlistStatus(tvShowId, inWatchlist).onSuccess {
            watchlistTvShowsRefresher()
        }
    }
}