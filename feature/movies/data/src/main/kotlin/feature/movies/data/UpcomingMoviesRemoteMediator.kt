package feature.movies.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.github.michaelbull.result.mapBoth
import core.data.network.NetworkResult
import core.database.feature.movies.upcoming.UpcomingMovieDao
import core.database.feature.movies.upcoming.UpcomingMovieEntity
import feature.movies.data.remote.response.UpcomingMoviesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class UpcomingMoviesRemoteMediator(
    private val getUpcomingMovies: suspend (page: Int) -> NetworkResult<UpcomingMoviesResponse>,
    private val upcomingMovieDao: UpcomingMovieDao,
) : RemoteMediator<Int, UpcomingMovieEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UpcomingMovieEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    withContext(Dispatchers.IO) { upcomingMovieDao.clearAll() }
                    1
                }

                LoadType.PREPEND ->
                    return MediatorResult.Success(endOfPaginationReached = true)

                LoadType.APPEND -> {
                    val lastPageNumber = withContext(Dispatchers.IO) {
                        upcomingMovieDao.getAll().lastOrNull()?.page
                    }

                    if (lastPageNumber == null) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    } else {
                        lastPageNumber + 1
                    }
                }
            }

            getUpcomingMovies.invoke(page).mapBoth(
                success = { response ->
                    withContext(Dispatchers.IO) {
                        upcomingMovieDao.insert(response.results.map {
                            UpcomingMovieEntity(
                                it.id,
                                page,
                                it.title,
                                it.posterPath,
                                it.voteAverage,
                                isFavorite = null
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