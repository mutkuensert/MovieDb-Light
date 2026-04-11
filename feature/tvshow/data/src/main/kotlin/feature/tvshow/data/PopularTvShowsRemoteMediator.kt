package feature.tvshow.data

import core.data.paging.GenericRemoteMediator
import core.data.common.model.TvShowsResponse
import core.data.network.NetworkResult
import core.database.feature.tvshows.popular.PopularTvShow
import core.database.feature.tvshows.popular.PopularTvShowDao
import core.database.feature.tvshows.popular.PopularTvShowEntity

class PopularTvShowsRemoteMediator(
    private val getTvShows: suspend (page: Int) -> NetworkResult<TvShowsResponse>,
    private val popularTvShowDao: PopularTvShowDao,
) : GenericRemoteMediator<TvShowsResponse, PopularTvShow>() {
    override suspend fun onFetchPaginatedData(page: Int): NetworkResult<TvShowsResponse> {
        return getTvShows(page)
    }

    override suspend fun onClearAllCachedData() {
        popularTvShowDao.clearAll()
    }

    override suspend fun onGetLastPageInCache(): Int? {
        return popularTvShowDao.getAll().lastOrNull()?.tvShow?.page
    }

    override suspend fun onInsertDataIntoCache(
        paginatedData: TvShowsResponse,
        page: Int
    ) {
        popularTvShowDao.insert(paginatedData.results.map {
            PopularTvShowEntity(
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
