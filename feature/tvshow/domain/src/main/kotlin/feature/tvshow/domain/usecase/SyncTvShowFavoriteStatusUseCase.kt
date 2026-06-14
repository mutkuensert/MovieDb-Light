package feature.tvshow.domain.usecase

import javax.inject.Inject

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onOk
import core.domain.Failure
import core.domain.account.AccountRepository
import core.domain.profile.FavoriteTvShowsRefresher

class SyncTvShowFavoriteStatusUseCase @Inject constructor(
    private val favoriteTvShowsRefresher: FavoriteTvShowsRefresher,
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(tvShowId: Int, favorite: Boolean): Result<Unit, Failure> {
        return accountRepository.syncTvShowFavoriteStatus(tvShowId, favorite).onOk {
            favoriteTvShowsRefresher()
        }
    }
}