package feature.movies.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.github.michaelbull.result.mapBoth
import core.data.network.NetworkResult
import core.database.feature.movies.popular.PopularMovieDao
import core.database.feature.movies.popular.PopularMovieEntity
import feature.movies.data.remote.response.PopularMoviesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class PopularMoviesRemoteMediator(
    private val getPopularMovies: suspend (page: Int) -> NetworkResult<PopularMoviesResponse>,
    private val popularMovieDao: PopularMovieDao,
) : RemoteMediator<Int, PopularMovieEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PopularMovieEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    withContext(Dispatchers.IO) { popularMovieDao.clearAll() }
                    1
                }

                LoadType.PREPEND ->
                    return MediatorResult.Success(endOfPaginationReached = true)

                LoadType.APPEND -> {
                    val lastPageNumber = withContext(Dispatchers.IO) {
                        popularMovieDao.getAll().lastOrNull()?.page
                    }

                    if (lastPageNumber == null) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    } else {
                        lastPageNumber + 1
                    }
                }
            }

            getPopularMovies.invoke(page).mapBoth(
                success = { response ->
                    withContext(Dispatchers.IO) {
                        popularMovieDao.insert(response.results.map {
                            PopularMovieEntity(
                                it.id,
                                page,
                                it.title,
                                it.posterPath,
                                it.voteAverage
                            )
                        })
                    }
                    MediatorResult.Success(endOfPaginationReached = response.results.isEmpty())
                },
                failure = {
                    Timber.w(it.message)
                    MediatorResult.Success(endOfPaginationReached = true)
                }
            )
        } catch (e: Exception) {
            Timber.e(e)
            MediatorResult.Error(e)
        }
    }
}