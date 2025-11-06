package core.domain

import com.github.michaelbull.result.Result

interface AccountRepository {

    suspend fun fetchAccountDetails(): Result<User, ErrorMessage>
    suspend fun fetchFavoriteMovies()
    suspend fun syncMovieFavoriteStatus(
        movieId: Int,
        isFavorite: Boolean,
    ): Result<Unit, ErrorMessage>

    suspend fun fetchWatchlistMovies()
    suspend fun syncMovieWatchlistStatus(
        movieId: Int,
        inWatchlist: Boolean,
    ): Result<Unit, ErrorMessage>
}