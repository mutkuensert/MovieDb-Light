package feature.movie.data

import core.data.paging.GenericRemoteMediator
import core.data.common.model.MoviesResponse
import core.data.network.NetworkResult
import core.database.feature.movies.upcoming.UpcomingMovie
import core.database.feature.movies.upcoming.UpcomingMovieDao
import core.database.feature.movies.upcoming.UpcomingMovieEntity

class UpcomingMoviesRemoteMediator(
    private val getMovies: suspend (page: Int) -> NetworkResult<MoviesResponse>,
    private val upcomingMovieDao: UpcomingMovieDao,
) : GenericRemoteMediator<MoviesResponse, UpcomingMovie>() {
    override suspend fun onFetchPaginatedData(page: Int): NetworkResult<MoviesResponse> {
        return getMovies(page)
    }

    override suspend fun onClearAllCachedData() {
        upcomingMovieDao.clearAll()
    }

    override suspend fun onGetLastPageInCache(): Int? {
        return upcomingMovieDao.getAll().lastOrNull()?.movie?.page
    }

    override suspend fun onInsertDataIntoCache(
        paginatedData: MoviesResponse,
        page: Int
    ) {
        upcomingMovieDao.insert(paginatedData.results.map {
            UpcomingMovieEntity(
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
