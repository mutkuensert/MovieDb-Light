package feature.tvshow.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.github.michaelbull.result.mapBoth
import core.data.common.model.ReviewDto
import core.data.common.model.ReviewsResponse
import core.data.network.NetworkResult
import timber.log.Timber

class TvShowReviewsPagingSource(
    private val getReviews: suspend (page: Int) -> NetworkResult<ReviewsResponse>,
) : PagingSource<Int, ReviewDto>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ReviewDto> {
        return try {
            val page = params.key ?: 1
            getReviews(page).mapBoth(
                success = { response ->
                    val totalPages = response.totalPages
                    val nextPage = if (totalPages != null && page < totalPages) {
                        page + 1
                    } else {
                        null
                    }
                    LoadResult.Page(
                        data = response.results ?: emptyList(),
                        prevKey = null,
                        nextKey = nextPage
                    )
                },
                failure = {
                    Timber.e(it.message)
                    LoadResult.Error(RuntimeException(it.message))
                }
            )
        } catch (e: Exception) {
            Timber.e(e)
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ReviewDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
