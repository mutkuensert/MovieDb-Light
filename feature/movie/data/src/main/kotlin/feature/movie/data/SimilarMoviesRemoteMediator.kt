package feature.movie.data

import core.data.model.common.MoviesResponse
import core.data.network.NetworkResult
import core.database.feature.movies.similar.SimilarMovie
import core.database.feature.movies.similar.SimilarMovieDao
import core.database.feature.movies.similar.SimilarMovieEntity

class SimilarMoviesRemoteMediator(
    private val getMovies: suspend (page: Int) -> NetworkResult<MoviesResponse>,
    private val similarMovieDao: SimilarMovieDao,
) : GenericRemoteMediator<MoviesResponse, SimilarMovie>() {
    override suspend fun onFetchPaginatedData(page: Int): NetworkResult<MoviesResponse> {
        return getMovies(page)
    }

    override suspend fun onClearAllCachedData() {
        similarMovieDao.clearAll()
    }

    override suspend fun onGetLastPageInCache(): Int? {
        return similarMovieDao.getAll().lastOrNull()?.movie?.page
    }

    override suspend fun onInsertDataIntoCache(
        paginatedData: MoviesResponse,
        page: Int
    ) {
        similarMovieDao.insert(paginatedData.results.map {
            SimilarMovieEntity(
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
