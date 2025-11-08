package feature.movie.data

import core.data.model.common.MoviesResponse
import core.data.network.NetworkResult
import core.database.feature.movies.toprated.TopRatedMovie
import core.database.feature.movies.toprated.TopRatedMovieDao
import core.database.feature.movies.toprated.TopRatedMovieEntity

class TopRatedMoviesRemoteMediator(
    private val getMovies: suspend (page: Int) -> NetworkResult<MoviesResponse>,
    private val topRatedMovieDao: TopRatedMovieDao
) : GenericRemoteMediator<MoviesResponse, TopRatedMovie>() {
    override suspend fun onFetchPaginatedData(page: Int): NetworkResult<MoviesResponse> {
        return getMovies(page)
    }

    override suspend fun onClearAllCachedData() {
        topRatedMovieDao.clearAll()
    }

    override suspend fun onGetLastPageInCache(): Int? {
        return topRatedMovieDao.getAll().lastOrNull()?.movie?.page
    }

    override suspend fun onInsertDataIntoCache(
        paginatedData: MoviesResponse,
        page: Int
    ) {
        topRatedMovieDao.insert(paginatedData.results.map {
            TopRatedMovieEntity(
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
