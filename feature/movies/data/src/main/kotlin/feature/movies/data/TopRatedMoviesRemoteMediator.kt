package feature.movies.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.github.michaelbull.result.mapBoth
import core.data.network.NetworkResult
import core.database.feature.movies.toprated.TopRatedMovieDao
import core.database.feature.movies.toprated.TopRatedMovieEntity
import core.database.feature.movies.toprated.TopRatedMovie
import feature.movies.data.remote.response.TopRatedMoviesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class TopRatedMoviesRemoteMediator(
    private val getTopRatedMovies: suspend (page: Int) -> NetworkResult<TopRatedMoviesResponse>,
    private val topRatedMovieDao: TopRatedMovieDao
) : RemoteMediator<Int, TopRatedMovie>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, TopRatedMovie>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    withContext(Dispatchers.IO) { topRatedMovieDao.clearAll() }
                    1
                }

                LoadType.PREPEND ->
                    return MediatorResult.Success(endOfPaginationReached = true)

                LoadType.APPEND -> {
                    val lastPageNumber = withContext(Dispatchers.IO) {
                        topRatedMovieDao.getAll().lastOrNull()?.movie?.page
                    }

                    if (lastPageNumber == null) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    } else {
                        lastPageNumber + 1
                    }
                }
            }

            getTopRatedMovies.invoke(page).mapBoth(
                success = { response ->
                    withContext(Dispatchers.IO) {
                        topRatedMovieDao.insert(response.results.map {
                            TopRatedMovieEntity(
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