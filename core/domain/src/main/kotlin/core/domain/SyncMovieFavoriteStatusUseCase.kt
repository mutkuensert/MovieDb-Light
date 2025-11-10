package core.domain

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onSuccess
import core.domain.profile.ProfileFavoriteMoviesPagingInvalidator

class SyncMovieFavoriteStatusUseCase(
    private val profileFavoriteMoviesPagingInvalidator: ProfileFavoriteMoviesPagingInvalidator,
    private val accountRepository: AccountRepository
) {
    suspend fun execute(movieId: Int, favorite: Boolean): Result<Unit, ErrorMessage> {
        return accountRepository.syncMovieFavoriteStatus(movieId, favorite).onSuccess {
            profileFavoriteMoviesPagingInvalidator.invalidateFavoriteMovies()
        }
    }
}
