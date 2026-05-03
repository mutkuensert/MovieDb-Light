package core.domain.account

import com.github.michaelbull.result.Result
import core.domain.Failure

interface AccountRepository {

    suspend fun getAccountDetails(): Result<User, Failure>
    suspend fun fetchFavoriteMovies(sortBy: SortBy.CreatedAt = SortBy.CreatedAt.ASCENDING)
    suspend fun syncMovieFavoriteStatus(
        movieId: Int,
        isFavorite: Boolean,
    ): Result<Unit, Failure>

    suspend fun fetchWatchlistMovies(sortBy: SortBy.CreatedAt = SortBy.CreatedAt.ASCENDING)
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
    suspend fun fetchFavoriteTvShows(sortBy: SortBy.CreatedAt)
    suspend fun fetchWatchlistTvShows(sortBy: SortBy.CreatedAt)
}
