package core.domain.account

import com.github.michaelbull.result.Result
import core.domain.Failure

interface AccountRepository {

    suspend fun getAccountDetails(): Result<User, Failure>
    suspend fun syncMovieFavoriteStatus(
        movieId: Int,
        isFavorite: Boolean,
    ): Result<Unit, Failure>

    suspend fun syncMovieWatchlistStatus(
        movieId: Int,
        inWatchlist: Boolean,
    ): Result<Unit, Failure>

    suspend fun syncTvShowFavoriteStatus(
        tvShowId: Int,
        isFavorite: Boolean,
    ): Result<Unit, Failure>

    suspend fun syncTvShowWatchlistStatus(
        tvShowId: Int,
        inWatchlist: Boolean,
    ): Result<Unit, Failure>

    suspend fun clearUserRelatedData()
}
