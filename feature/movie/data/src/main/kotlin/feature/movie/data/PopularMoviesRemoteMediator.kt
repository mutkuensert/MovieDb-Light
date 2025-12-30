package feature.movie.data

import core.data.paging.GenericRemoteMediator
import core.data.common.model.MoviesResponse
import core.data.network.NetworkResult
import core.database.feature.movies.popular.PopularMovie
import core.database.feature.movies.popular.PopularMovieDao
import core.database.feature.movies.popular.PopularMovieEntity

class PopularMoviesRemoteMediator(
    private val getMovies: suspend (page: Int) -> NetworkResult<MoviesResponse>,
    private val popularMovieDao: PopularMovieDao,
) : GenericRemoteMediator<MoviesResponse, PopularMovie>() {
    override suspend fun onFetchPaginatedData(page: Int): NetworkResult<MoviesResponse> {
        return getMovies(page)
    }

    override suspend fun onClearAllCachedData() {
        popularMovieDao.clearAll()
    }

    override suspend fun onGetLastPageInCache(): Int? {
        return popularMovieDao.getAll().lastOrNull()?.movie?.page
    }

    override suspend fun onInsertDataIntoCache(
        paginatedData: MoviesResponse,
        page: Int
    ) {
        popularMovieDao.insert(paginatedData.results.map {
            PopularMovieEntity(
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
