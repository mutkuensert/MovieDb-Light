package core.domain

import com.github.michaelbull.result.Result

interface AccountRepository {

    suspend fun fetchAccountDetails(): Result<User, ErrorMessage>
    suspend fun fetchFavoriteMovies()
    suspend fun syncMovieFavoriteStatus(
        isFavorite: Boolean,
        movieId: Int
    ): Result<Unit, ErrorMessage>

    suspend fun fetchWatchlistMovies()
    suspend fun syncMovieWatchlistStatus(
        inWatchlist: Boolean,
        movieId: Int
    ): Result<Unit, ErrorMessage>
}