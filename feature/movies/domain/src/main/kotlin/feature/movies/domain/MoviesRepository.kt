package feature.movies.domain

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {
    fun getPopularMovies(): Flow<PagingData<Movie>>
    fun getMoviesNowPlaying(): Flow<PagingData<Movie>>
    fun getUpcomingMovies(): Flow<PagingData<Movie>>
    fun getTopRatedMovies(): Flow<PagingData<Movie>>
}