package feature.movies.domain

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {
    fun getPopularMovies(): Flow<PagingData<Movie>>
    fun getMoviesNowPlaying(): Flow<PagingData<Movie>>
    suspend fun addToFavorites(movieId: Int)
    suspend fun removeFromFavorites(movieId: Int)
}