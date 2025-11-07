package feature.profile.domain

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getFavoriteMovies(): Flow<PagingData<Movie>>
    fun getWatchlistMovies(): Flow<PagingData<Movie>>
    fun getRatedMovies(): Flow<PagingData<Movie>>
}