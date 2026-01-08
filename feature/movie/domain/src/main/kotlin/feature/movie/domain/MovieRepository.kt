package feature.movie.domain

import androidx.paging.PagingData
import com.github.michaelbull.result.Result
import core.domain.Failure
import core.domain.common.model.Provider
import feature.movie.domain.model.AccountStates
import feature.movie.domain.model.Movie
import feature.movie.domain.model.MovieDetails
import feature.movie.domain.model.Person
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getPopularMovies(countryCode: String? = null): Flow<PagingData<Movie>>
    fun getMoviesNowPlaying(countryCode: String? = null): Flow<PagingData<Movie>>
    fun getUpcomingMovies(countryCode: String? = null): Flow<PagingData<Movie>>
    fun getTopRatedMovies(countryCode: String? = null): Flow<PagingData<Movie>>
    fun getSimilarMovies(movieId: Int): Flow<PagingData<Movie>>
    suspend fun getMovieDetails(movieId: Int): Result<MovieDetails, Failure>
    suspend fun getMovieCast(movieId: Int): Result<List<Person>, Failure>
    suspend fun getProviders(movieId: Int): Result<List<Provider>, Failure>
    suspend fun getTrailerUrl(movieId: Int): Result<String?, Failure>
    suspend fun getAccountStates(movieId: Int): Result<AccountStates, Failure>
    suspend fun rateMovie(movieId: Int, rating: Int): Result<Unit, Failure>
    suspend fun removeRating(movieId: Int): Result<Unit, Failure>
    fun updateLanguageRelatedData()
    suspend fun getImagePaths(movieId: Int): Result<List<String>, Failure>
}