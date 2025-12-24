package core.domain.account

import com.github.michaelbull.result.Result
import core.domain.Failure
import core.domain.User

interface AccountRepository {

    suspend fun fetchAccountDetails(): Result<User, Failure>
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
}