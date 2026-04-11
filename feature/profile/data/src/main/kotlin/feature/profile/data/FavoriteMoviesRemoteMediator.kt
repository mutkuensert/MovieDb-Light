package feature.profile.data

import core.data.common.model.MoviesResponse
import core.data.network.NetworkResult
import core.data.paging.GenericRemoteMediator
import core.database.account.FavoriteMovieDao
import core.database.account.model.FavoriteMovieEntity

class FavoriteMoviesRemoteMediator(
    private val getMovies: suspend (page: Int) -> NetworkResult<MoviesResponse>,
    private val favoriteMovieDao: FavoriteMovieDao,
) : GenericRemoteMediator<MoviesResponse, FavoriteMovieEntity>() {
    override suspend fun onFetchPaginatedData(page: Int): NetworkResult<MoviesResponse> {
        return getMovies(page)
    }

    override suspend fun onClearAllCachedData() {
        favoriteMovieDao.clearAllMovies()
    }

    override suspend fun onGetLastPageInCache(): Int? {
        return favoriteMovieDao.getAllMovies().lastOrNull()?.page
    }

    override suspend fun onInsertDataIntoCache(
        paginatedData: MoviesResponse,
        page: Int
    ) {
        favoriteMovieDao.insertMovies(paginatedData.results.map {
            FavoriteMovieEntity(
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
