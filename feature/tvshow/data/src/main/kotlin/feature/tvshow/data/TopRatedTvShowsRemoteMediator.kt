package feature.tvshow.data

import core.data.paging.GenericRemoteMediator
import core.data.common.model.TvShowsResponse
import core.data.network.NetworkResult
import core.database.feature.tvshows.toprated.TopRatedTvShow
import core.database.feature.tvshows.toprated.TopRatedTvShowDao
import core.database.feature.tvshows.toprated.TopRatedTvShowEntity

class TopRatedTvShowsRemoteMediator(
    private val getTvShows: suspend (page: Int) -> NetworkResult<TvShowsResponse>,
    private val topRatedTvShowDao: TopRatedTvShowDao
) : GenericRemoteMediator<TvShowsResponse, TopRatedTvShow>() {
    override suspend fun onFetchPaginatedData(page: Int): NetworkResult<TvShowsResponse> {
        return getTvShows(page)
    }

    override suspend fun onClearAllCachedData() {
        topRatedTvShowDao.clearAll()
    }

    override suspend fun onGetLastPageInCache(): Int? {
        return topRatedTvShowDao.getAll().lastOrNull()?.tvShow?.page
    }

    override suspend fun onInsertDataIntoCache(
        paginatedData: TvShowsResponse,
        page: Int
    ) {
        topRatedTvShowDao.insert(paginatedData.results.map {
            TopRatedTvShowEntity(
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
