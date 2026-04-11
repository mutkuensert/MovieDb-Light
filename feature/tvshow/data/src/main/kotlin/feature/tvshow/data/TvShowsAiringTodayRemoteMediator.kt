package feature.tvshow.data

import core.data.paging.GenericRemoteMediator
import core.data.common.model.TvShowsResponse
import core.data.network.NetworkResult
import core.database.feature.tvshows.airingtoday.TvShowAiringToday
import core.database.feature.tvshows.airingtoday.TvShowsAiringTodayDao
import core.database.feature.tvshows.airingtoday.TvShowAiringTodayEntity

class TvShowsAiringTodayRemoteMediator(
    private val getTvShows: suspend (page: Int) -> NetworkResult<TvShowsResponse>,
    private val tvShowsAiringTodayDao: TvShowsAiringTodayDao,
) : GenericRemoteMediator<TvShowsResponse, TvShowAiringToday>() {
    override suspend fun onFetchPaginatedData(page: Int): NetworkResult<TvShowsResponse> {
        return getTvShows(page)
    }

    override suspend fun onClearAllCachedData() {
        tvShowsAiringTodayDao.clearAll()
    }

    override suspend fun onGetLastPageInCache(): Int? {
        return tvShowsAiringTodayDao.getAll().lastOrNull()?.tvShow?.page
    }

    override suspend fun onInsertDataIntoCache(
        paginatedData: TvShowsResponse,
        page: Int
    ) {
        tvShowsAiringTodayDao.insert(paginatedData.results.map {
            TvShowAiringTodayEntity(
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
