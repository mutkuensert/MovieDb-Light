package core.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.github.michaelbull.result.mapBoth
import core.data.common.model.TvShowDto
import core.data.common.model.TvShowsResponse
import core.data.network.NetworkResult
import timber.log.Timber

class TvShowsPagingSource(
    private val getTvShows: suspend (page: Int) -> NetworkResult<TvShowsResponse>,
) : PagingSource<Int, TvShowDto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TvShowDto> {
        return try {
            val page = params.key ?: 1
            getTvShows(page).mapBoth(
                success = { response ->
                    LoadResult.Page(
                        data = response.results,
                        prevKey = null,
                        nextKey = if (response.page < response.totalPages) {
                            response.page + 1
                        } else {
                            null
                        },
                    )
                },
                failure = { error ->
                    Timber.e(error.message)
                    LoadResult.Error(RuntimeException(error.message))
                },
            )
        } catch (exception: Exception) {
            Timber.e(exception)
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, TvShowDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
