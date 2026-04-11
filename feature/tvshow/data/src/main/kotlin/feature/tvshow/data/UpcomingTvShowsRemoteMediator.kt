package feature.tvshow.data

import core.data.paging.GenericRemoteMediator
import core.data.common.model.TvShowsResponse
import core.data.network.NetworkResult
import core.database.feature.tvshows.upcoming.UpcomingTvShow
import core.database.feature.tvshows.upcoming.UpcomingTvShowDao
import core.database.feature.tvshows.upcoming.UpcomingTvShowEntity

class UpcomingTvShowsRemoteMediator(
    private val getUpcomingTvShows: suspend (page: Int) -> NetworkResult<TvShowsResponse>,
    private val upcomingTvShowDao: UpcomingTvShowDao,
) : GenericRemoteMediator<TvShowsResponse, UpcomingTvShow>() {
    override suspend fun onFetchPaginatedData(page: Int): NetworkResult<TvShowsResponse> {
        return getUpcomingTvShows(page)
    }

    override suspend fun onClearAllCachedData() {
        upcomingTvShowDao.clearAll()
    }

    override suspend fun onGetLastPageInCache(): Int? {
        return upcomingTvShowDao.getAll().lastOrNull()?.tvShow?.page
    }

    override suspend fun onInsertDataIntoCache(
        paginatedData: TvShowsResponse,
        page: Int
    ) {
        upcomingTvShowDao.insert(paginatedData.results.map {
            UpcomingTvShowEntity(
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
