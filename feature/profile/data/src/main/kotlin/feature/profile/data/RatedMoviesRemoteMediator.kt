package feature.profile.data

import core.data.common.model.MoviesResponse
import core.data.network.NetworkResult
import core.data.paging.GenericRemoteMediator
import core.database.account.RatedMovieDao
import core.database.account.model.RatedMovieEntity

class RatedMoviesRemoteMediator(
    private val getMovies: suspend (page: Int) -> NetworkResult<MoviesResponse>,
    private val ratedMovieDao: RatedMovieDao,
) : GenericRemoteMediator<MoviesResponse, RatedMovieEntity>() {
    override suspend fun onFetchPaginatedData(page: Int): NetworkResult<MoviesResponse> {
        return getMovies(page)
    }

    override suspend fun onClearAllCachedData() {
        ratedMovieDao.clearAllMovies()
    }

    override suspend fun onGetLastPageInCache(): Int? {
        return ratedMovieDao.getAllMovies().lastOrNull()?.page
    }

    override suspend fun onInsertDataIntoCache(
        paginatedData: MoviesResponse,
        page: Int
    ) {
        ratedMovieDao.insert(paginatedData.results.map {
            RatedMovieEntity(
                it.id,
                page,
                it.title,
                it.posterPath,
                it.voteAverage
            )
        })
    }

    override suspend fun isEndOfPaginationReached(paginatedData: MoviesResponse): Boolean {
        return paginatedData.results.isEmpty()
    }
}
