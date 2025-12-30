package feature.search.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.github.michaelbull.result.mapBoth
import core.data.network.NetworkResult

class SearchPagingSource(
    val search: suspend (page: Int) -> NetworkResult<SearchResponse>,
) : PagingSource<Int, SearchResultDto>() {
    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, SearchResultDto> {
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
                    LoadResult.Error(RuntimeException(it.message))
                })
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, SearchResultDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}