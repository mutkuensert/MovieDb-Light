package feature.tvshow.domain.usecase

import javax.inject.Inject

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onOk
import core.domain.Failure
import core.domain.account.AccountRepository
import core.domain.profile.WatchlistTvShowsRefresher

class SyncTvShowWatchlistStatusUseCase @Inject constructor(
    private val watchlistTvShowsRefresher: WatchlistTvShowsRefresher,
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(tvShowId: Int, inWatchlist: Boolean): Result<Unit, Failure> {
        return accountRepository.syncTvShowWatchlistStatus(tvShowId, inWatchlist).onOk {
            watchlistTvShowsRefresher()
        }
    }
}