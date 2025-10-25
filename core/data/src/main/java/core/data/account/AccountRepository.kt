package core.data.account

import com.github.michaelbull.result.Result
import core.domain.ErrorMessage

interface AccountRepository {

    suspend fun fetchUserDetails(): Result<Unit, ErrorMessage>
    suspend fun fetchFavoriteMovies()
    suspend fun syncMovieFavoriteStatus(isFavorite: Boolean, movieId: Int): Result<Unit, ErrorMessage>
}
