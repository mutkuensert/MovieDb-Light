package feature.profile.data

import core.data.common.model.TvShowsResponse
import core.data.network.NetworkResult
import core.data.paging.GenericRemoteMediator
import core.database.account.WatchlistTvShowDao
import core.database.account.model.WatchlistTvShowEntity

class WatchlistTvShowsRemoteMediator(
    private val getTvShows: suspend (page: Int) -> NetworkResult<TvShowsResponse>,
    private val watchlistTvShowDao: WatchlistTvShowDao,
) : GenericRemoteMediator<TvShowsResponse, WatchlistTvShowEntity>() {
    override suspend fun onFetchPaginatedData(page: Int): NetworkResult<TvShowsResponse> {
        return getTvShows(page)
    }

    override suspend fun onClearAllCachedData() {
        watchlistTvShowDao.clearAllTvShows()
    }

    override suspend fun onGetLastPageInCache(): Int? {
        return watchlistTvShowDao.getAll().lastOrNull()?.page
    }

    override suspend fun onInsertDataIntoCache(paginatedData: TvShowsResponse, page: Int) {
        watchlistTvShowDao.insertTvShows(paginatedData.results.map {
            WatchlistTvShowEntity(
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
