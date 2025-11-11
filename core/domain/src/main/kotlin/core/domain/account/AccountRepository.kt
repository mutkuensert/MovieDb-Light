package core.domain.account

import com.github.michaelbull.result.Result
import core.domain.ErrorMessage
import core.domain.User

interface AccountRepository {

    suspend fun fetchAccountDetails(): Result<User, ErrorMessage>
    suspend fun fetchFavoriteMovies(sortBy: SortBy.CreatedAt = SortBy.CreatedAt.ASCENDING)
    suspend fun syncMovieFavoriteStatus(
        movieId: Int,
        isFavorite: Boolean,
    ): Result<Unit, ErrorMessage>

    suspend fun fetchWatchlistMovies(sortBy: SortBy.CreatedAt = SortBy.CreatedAt.ASCENDING)
    suspend fun syncMovieWatchlistStatus(
        movieId: Int,
        inWatchlist: Boolean,
    ): Result<Unit, ErrorMessage>
}