package feature.profile.data

import core.data.model.common.MoviesResponse
import core.data.network.NetworkResult
import core.data.paging.GenericRemoteMediator
import core.database.account.WatchlistMovieDao
import core.database.account.model.WatchlistMovieEntity

class WatchlistMoviesRemoteMediator(
    private val getMovies: suspend (page: Int) -> NetworkResult<MoviesResponse>,
    private val watchlistMovieDao: WatchlistMovieDao,
) : GenericRemoteMediator<MoviesResponse, WatchlistMovieEntity>() {
    override suspend fun onFetchPaginatedData(page: Int): NetworkResult<MoviesResponse> {
        return getMovies(page)
    }

    override suspend fun onClearAllCachedData() {
        watchlistMovieDao.clearAllMovies()
    }

    override suspend fun onGetLastPageInCache(): Int? {
        return watchlistMovieDao.getAllMovies().lastOrNull()?.page
    }

    override suspend fun onInsertDataIntoCache(
        paginatedData: MoviesResponse,
        page: Int
    ) {
        watchlistMovieDao.insertMovie(paginatedData.results.map {
            WatchlistMovieEntity(
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
