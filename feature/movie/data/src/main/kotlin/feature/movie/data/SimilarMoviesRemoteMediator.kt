package feature.movie.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.github.michaelbull.result.mapBoth
import core.data.network.NetworkResult
import core.database.feature.movies.similar.SimilarMovie
import core.database.feature.movies.similar.SimilarMovieDao
import core.database.feature.movies.similar.SimilarMovieEntity
import core.data.model.common.MoviesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class SimilarMoviesRemoteMediator(
    private val getSimilarMovies: suspend (page: Int) -> NetworkResult<MoviesResponse>,
    private val similarMovieDao: SimilarMovieDao,
) : RemoteMediator<Int, SimilarMovie>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, SimilarMovie>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    withContext(Dispatchers.IO) { similarMovieDao.clearAll() }
                    1
                }

                LoadType.PREPEND ->
                    return MediatorResult.Success(endOfPaginationReached = true)

                LoadType.APPEND -> {
                    val lastPageNumber = withContext(Dispatchers.IO) {
                        similarMovieDao.getAll().lastOrNull()?.movie?.page
                    }

                    if (lastPageNumber == null) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    } else {
                        lastPageNumber + 1
                    }
                }
            }

            getSimilarMovies.invoke(page).mapBoth(
                success = { response ->
                    withContext(Dispatchers.IO) {
                        similarMovieDao.insert(response.results.map {
                            SimilarMovieEntity(
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
