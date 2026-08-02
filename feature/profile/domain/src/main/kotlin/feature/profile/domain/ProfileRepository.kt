package feature.profile.domain

import androidx.paging.PagingData
import core.domain.account.SortBy
import feature.profile.domain.model.Production
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getFavoriteMovies(sortBy: SortBy.CreatedAt = SortBy.CreatedAt.ASCENDING): Flow<PagingData<Production>>
    fun getWatchlistMovies(sortBy: SortBy.CreatedAt = SortBy.CreatedAt.ASCENDING): Flow<PagingData<Production>>
    fun getRatedMovies(sortBy: SortBy.CreatedAt = SortBy.CreatedAt.ASCENDING): Flow<PagingData<Production>>
    fun getFavoriteTvShows(sortBy: SortBy.CreatedAt = SortBy.CreatedAt.ASCENDING): Flow<PagingData<Production>>
    fun getWatchlistTvShows(sortBy: SortBy.CreatedAt = SortBy.CreatedAt.ASCENDING): Flow<PagingData<Production>>
    fun getRatedTvShows(sortBy: SortBy.CreatedAt = SortBy.CreatedAt.ASCENDING): Flow<PagingData<Production>>
    fun updateFavoriteMovies()
    fun updateRatedMovies()
    fun updateWatchlistMovies()
    fun updateFavoriteTvShows()
    fun updateRatedTvShows()
    fun updateWatchlistTvShows()
    fun updateLanguageRelatedData()
}
