package core.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.github.michaelbull.result.mapBoth
import core.data.common.model.MovieDto
import core.data.common.model.MoviesResponse
import core.data.network.NetworkResult
import timber.log.Timber

class MoviesPagingSource(
    private val getMovies: suspend (page: Int) -> NetworkResult<MoviesResponse>,
) : PagingSource<Int, MovieDto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieDto> {
        return try {
            val page = params.key ?: 1
            getMovies(page).mapBoth(
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

    override fun getRefreshKey(state: PagingState<Int, MovieDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
