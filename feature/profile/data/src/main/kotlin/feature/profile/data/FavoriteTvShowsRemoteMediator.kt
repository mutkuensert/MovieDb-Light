package feature.profile.data

import core.data.common.model.TvShowsResponse
import core.data.network.NetworkResult
import core.data.paging.GenericRemoteMediator
import core.database.account.FavoriteTvShowDao
import core.database.account.model.FavoriteTvShowEntity

class FavoriteTvShowsRemoteMediator(
    private val getTvShows: suspend (page: Int) -> NetworkResult<TvShowsResponse>,
    private val favoriteTvShowDao: FavoriteTvShowDao,
) : GenericRemoteMediator<TvShowsResponse, FavoriteTvShowEntity>() {
    override suspend fun onFetchPaginatedData(page: Int): NetworkResult<TvShowsResponse> {
        return getTvShows(page)
    }

    override suspend fun onClearAllCachedData() {
        favoriteTvShowDao.clearAllTvShows()
    }

    override suspend fun onGetLastPageInCache(): Int? {
        return favoriteTvShowDao.getAllTvShows().lastOrNull()?.page
    }

    override suspend fun onInsertDataIntoCache(paginatedData: TvShowsResponse, page: Int) {
        favoriteTvShowDao.insertTvShows(paginatedData.results.map {
            FavoriteTvShowEntity(
                it.id,
                page,
                it.name,
                it.posterPath,
                it.voteAverage
            )
        })
    }

    override suspend fun isEndOfPaginationReached(paginatedData: TvShowsResponse): Boolean {
        return paginatedData.results.isEmpty()
    }
}
