package feature.movie.data

import core.data.paging.GenericRemoteMediator
import core.data.common.model.MoviesResponse
import core.data.network.NetworkResult
import core.database.feature.movies.nowplaying.NowPlayingMovie
import core.database.feature.movies.nowplaying.NowPlayingMovieDao
import core.database.feature.movies.nowplaying.NowPlayingMovieEntity

class NowPlayingMoviesRemoteMediator(
    private val getMovies: suspend (page: Int) -> NetworkResult<MoviesResponse>,
    private val nowPlayingMovieDao: NowPlayingMovieDao,
) : GenericRemoteMediator<MoviesResponse, NowPlayingMovie>() {
    override suspend fun onFetchPaginatedData(page: Int): NetworkResult<MoviesResponse> {
        return getMovies(page)
    }

    override suspend fun onClearAllCachedData() {
        nowPlayingMovieDao.clearAll()
    }

    override suspend fun onGetLastPageInCache(): Int? {
        return nowPlayingMovieDao.getAll().lastOrNull()?.movie?.page
    }

    override suspend fun onInsertDataIntoCache(
        paginatedData: MoviesResponse,
        page: Int
    ) {
        nowPlayingMovieDao.insert(paginatedData.results.map {
            NowPlayingMovieEntity(
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
