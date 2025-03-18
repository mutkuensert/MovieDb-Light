package feature.movies.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.github.michaelbull.result.mapBoth
import core.data.network.NetworkResult
import core.database.feature.movies.nowplaying.NowPlayingMovieDao
import core.database.feature.movies.nowplaying.NowPlayingMovieEntity
import feature.movies.data.remote.response.NowPlayingMoviesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class NowPlayingMoviesRemoteMediator(
    private val getNowPlayingMovies: suspend (page: Int) -> NetworkResult<NowPlayingMoviesResponse>,
    private val nowPlayingMovieDao: NowPlayingMovieDao,
) : RemoteMediator<Int, NowPlayingMovieEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, NowPlayingMovieEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    withContext(Dispatchers.IO) { nowPlayingMovieDao.clearAll() }
                    1
                }

                LoadType.PREPEND ->
                    return MediatorResult.Success(endOfPaginationReached = true)

                LoadType.APPEND -> {
                    val lastPageNumber = withContext(Dispatchers.IO) {
                        nowPlayingMovieDao.getAll().lastOrNull()?.page
                    }

                    if (lastPageNumber == null) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    } else {
                        lastPageNumber + 1
                    }
                }
            }

            getNowPlayingMovies.invoke(page).mapBoth(
                success = { response ->
                    withContext(Dispatchers.IO) {
                        nowPlayingMovieDao.insert(response.results.map {
                            NowPlayingMovieEntity(
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