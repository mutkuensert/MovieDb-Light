package feature.tvshow.data

import core.data.paging.GenericRemoteMediator
import core.data.common.model.TvShowsResponse
import core.data.network.NetworkResult
import core.database.feature.tvshows.similar.SimilarTvShow
import core.database.feature.tvshows.similar.SimilarTvShowDao
import core.database.feature.tvshows.similar.SimilarTvShowEntity

class SimilarTvShowsRemoteMediator(
    private val getTvShows: suspend (page: Int) -> NetworkResult<TvShowsResponse>,
    private val similarTvShowDao: SimilarTvShowDao,
) : GenericRemoteMediator<TvShowsResponse, SimilarTvShow>() {
    override suspend fun onFetchPaginatedData(page: Int): NetworkResult<TvShowsResponse> {
        return getTvShows(page)
    }

    override suspend fun onClearAllCachedData() {
        similarTvShowDao.clearAll()
    }

    override suspend fun onGetLastPageInCache(): Int? {
        return similarTvShowDao.getAll().lastOrNull()?.tvShow?.page
    }

    override suspend fun onInsertDataIntoCache(
        paginatedData: TvShowsResponse,
        page: Int
    ) {
        similarTvShowDao.insert(paginatedData.results.map {
            SimilarTvShowEntity(
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
