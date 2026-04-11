package feature.profile.data

import core.data.common.model.TvShowsResponse
import core.data.network.NetworkResult
import core.data.paging.GenericRemoteMediator
import core.database.account.RatedTvShowDao
import core.database.account.model.RatedTvShowEntity

class RatedTvShowsRemoteMediator(
    private val getTvShows: suspend (page: Int) -> NetworkResult<TvShowsResponse>,
    private val ratedTvShowDao: RatedTvShowDao,
) : GenericRemoteMediator<TvShowsResponse, RatedTvShowEntity>() {
    override suspend fun onFetchPaginatedData(page: Int): NetworkResult<TvShowsResponse> {
        return getTvShows(page)
    }

    override suspend fun onClearAllCachedData() {
        ratedTvShowDao.clearAll()
    }

    override suspend fun onGetLastPageInCache(): Int? {
        return ratedTvShowDao.getAll().lastOrNull()?.page
    }

    override suspend fun onInsertDataIntoCache(paginatedData: TvShowsResponse, page: Int) {
        ratedTvShowDao.insertTvShows(paginatedData.results.map {
            RatedTvShowEntity(
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
