package feature.profile.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapBoth
import core.data.model.common.MovieDto
import core.data.model.common.MoviesResponse
import core.data.network.NetworkError

class GenericMoviesPagingSource(
    val getMovies: suspend (page: Int) -> Result<MoviesResponse, NetworkError>
) : PagingSource<Int, MovieDto>() {
    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, MovieDto> {
        try {
            val page = params.key ?: 1
            return getMovies(page).mapBoth(success = {
                LoadResult.Page(
                    data = it.results,
                    prevKey = null,
                    nextKey = if (it.results.isNotEmpty()) it.page + 1 else null
                )
            }, failure = {
                LoadResult.Error(RuntimeException(it.message))
            })
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}