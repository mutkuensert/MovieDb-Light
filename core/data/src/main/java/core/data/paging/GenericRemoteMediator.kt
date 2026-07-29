package core.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.github.michaelbull.result.mapBoth
import core.data.network.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * @property T is paginated data.
 * @property R is cached entity in database
 */
@OptIn(ExperimentalPagingApi::class)
abstract class GenericRemoteMediator<T, R : Any> : RemoteMediator<Int, R>() {

    abstract suspend fun onFetchPaginatedData(page: Int): NetworkResult<T>
    abstract suspend fun onClearAllCachedData()
    abstract suspend fun onGetLastPageInCache(): Int?
    abstract suspend fun onInsertDataIntoCache(paginatedData: T, page: Int)
    abstract suspend fun isEndOfPaginationReached(paginatedData: T): Boolean

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, R>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    withContext(Dispatchers.IO) { onClearAllCachedData() }
                    1
                }

                LoadType.PREPEND ->
                    return MediatorResult.Success(endOfPaginationReached = true)

                LoadType.APPEND -> {
                    val lastPageNumber = withContext(Dispatchers.IO) {
                        onGetLastPageInCache()
                    }

                    if (lastPageNumber == null) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    } else {
                        lastPageNumber + 1
                    }
                }
            }

            onFetchPaginatedData(page).mapBoth(
                success = { response ->
                    withContext(Dispatchers.IO) {
                        onInsertDataIntoCache(response, page)
                    }
                    MediatorResult.Success(
                        endOfPaginationReached = isEndOfPaginationReached(
                            response
                        )
                    )
                },
                failure = {
                    Timber.w(it.message)
                    MediatorResult.Error(RuntimeException(it.message))
                }
            )
        } catch (e: Exception) {
            Timber.e(e)
            MediatorResult.Error(e)
        }
    }
}