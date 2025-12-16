package core.domain.movie

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onSuccess
import core.domain.ErrorMessage
import core.domain.account.AccountRepository
import core.domain.profile.FavoriteMoviesRefresher

class SyncMovieFavoriteStatusUseCase(
    private val favoriteMoviesRefresher: FavoriteMoviesRefresher,
    private val accountRepository: AccountRepository
) {
    suspend fun execute(movieId: Int, favorite: Boolean): Result<Unit, ErrorMessage> {
        return accountRepository.syncMovieFavoriteStatus(movieId, favorite).onSuccess {
            favoriteMoviesRefresher.refreshFavoriteMovies()
        }
    }
}