package feature.movie.domain.usecase

import javax.inject.Inject

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onOk
import core.domain.Failure
import core.domain.account.AccountRepository
import core.domain.profile.FavoriteMoviesRefresher

class SyncMovieFavoriteStatusUseCase @Inject constructor(
    private val favoriteMoviesRefresher: FavoriteMoviesRefresher,
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(movieId: Int, favorite: Boolean): Result<Unit, Failure> {
        return accountRepository.syncMovieFavoriteStatus(movieId, favorite).onOk {
            favoriteMoviesRefresher()
        }
    }
}