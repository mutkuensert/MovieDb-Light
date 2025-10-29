package core.domain

import com.github.michaelbull.result.Result

interface AccountRepository {

    suspend fun fetchAccountDetails(): Result<Unit, ErrorMessage>
    suspend fun fetchFavoriteMovies()
    suspend fun syncMovieFavoriteStatus(
        isFavorite: Boolean,
        movieId: Int
    ): Result<Unit, ErrorMessage>

    fun getUser(): User
}