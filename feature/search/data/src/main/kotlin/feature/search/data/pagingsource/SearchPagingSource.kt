package feature.search.data.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.github.michaelbull.result.mapBoth
import core.data.network.NetworkResult
import feature.search.data.MultiSearchResponse
import feature.search.data.MultiSearchResultDto
import timber.log.Timber

class SearchPagingSource(
    val search: suspend (page: Int) -> NetworkResult<MultiSearchResponse>,
) : PagingSource<Int, MultiSearchResultDto>() {
    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, MultiSearchResultDto> {
        try {
            val nextPageNumber = params.key ?: 1
            return search(nextPageNumber).mapBoth(
                success = { response ->
                    LoadResult.Page(
                        data = response.results,
                        prevKey = null,
                        nextKey = if (response.results.isNotEmpty()) response.page + 1 else null
                    )
                },
                failure = {
                    Timber.e(it.message)
                    LoadResult.Error(RuntimeException(it.message))
                })
        } catch (e: Exception) {
            Timber.e(e)
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MultiSearchResultDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}