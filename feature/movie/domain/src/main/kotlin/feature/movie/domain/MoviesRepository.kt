package feature.movie.domain

import androidx.paging.PagingData
import com.github.michaelbull.result.Result
import core.domain.ErrorMessage
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {
    fun getPopularMovies(countryCode: String? = null): Flow<PagingData<Movie>>
    fun getMoviesNowPlaying(countryCode: String? = null): Flow<PagingData<Movie>>
    fun getUpcomingMovies(countryCode: String? = null): Flow<PagingData<Movie>>
    fun getTopRatedMovies(countryCode: String? = null): Flow<PagingData<Movie>>
    suspend fun getMovieDetails(movieId: Int): Result<MovieDetails, ErrorMessage>
    suspend fun getMovieCast(movieId: Int): Result<List<Person>, ErrorMessage>
}