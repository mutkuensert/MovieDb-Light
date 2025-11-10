package feature.movie.domain

import androidx.paging.PagingData
import com.github.michaelbull.result.Result
import core.domain.ErrorMessage
import core.domain.model.AccountStates
import core.domain.model.Provider
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getPopularMovies(countryCode: String? = null): Flow<PagingData<Movie>>
    fun getMoviesNowPlaying(countryCode: String? = null): Flow<PagingData<Movie>>
    fun getUpcomingMovies(countryCode: String? = null): Flow<PagingData<Movie>>
    fun getTopRatedMovies(countryCode: String? = null): Flow<PagingData<Movie>>
    fun getSimilarMovies(movieId: Int): Flow<PagingData<Movie>>
    suspend fun getMovieDetails(movieId: Int): Result<MovieDetails, ErrorMessage>
    suspend fun getMovieCast(movieId: Int): Result<List<Person>, ErrorMessage>
    suspend fun getProviders(movieId: Int): Result<List<Provider>, ErrorMessage>
    suspend fun getTrailerUrl(movieId: Int): Result<String?, ErrorMessage>
    suspend fun getAccountStates(movieId: Int): Result<AccountStates, ErrorMessage>
    suspend fun rateMovie(movieId: Int, rating: Int): Result<Unit, ErrorMessage>
    suspend fun removeRating(movieId: Int): Result<Unit, ErrorMessage>
}