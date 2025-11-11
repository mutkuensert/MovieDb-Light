package feature.profile.domain

import androidx.paging.PagingData
import core.domain.account.SortBy
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getFavoriteMovies(sortBy: SortBy.CreatedAt = SortBy.CreatedAt.ASCENDING): Flow<PagingData<Movie>>
    fun getWatchlistMovies(sortBy: SortBy.CreatedAt = SortBy.CreatedAt.ASCENDING): Flow<PagingData<Movie>>
    fun getRatedMovies(sortBy: SortBy.CreatedAt = SortBy.CreatedAt.ASCENDING): Flow<PagingData<Movie>>
}