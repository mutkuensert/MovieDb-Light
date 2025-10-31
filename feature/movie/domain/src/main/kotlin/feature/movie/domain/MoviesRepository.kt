package feature.movie.domain

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {
    fun getPopularMovies(countryCode: String? = null): Flow<PagingData<Movie>>
    fun getMoviesNowPlaying(countryCode: String? = null): Flow<PagingData<Movie>>
    fun getUpcomingMovies(countryCode: String? = null): Flow<PagingData<Movie>>
    fun getTopRatedMovies(countryCode: String? = null): Flow<PagingData<Movie>>
}