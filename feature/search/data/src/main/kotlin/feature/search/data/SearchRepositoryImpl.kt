package feature.search.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import feature.search.data.pagingsource.SearchPagingSource
import feature.search.data.pagingsource.TrendingPagingSource
import feature.search.domain.MultiResult
import feature.search.domain.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchRepositoryImpl(
    private val searchService: SearchService,
    private val trendingService: TrendingService,
) : SearchRepository {
    override fun search(query: String): Flow<PagingData<MultiResult>> {
        return Pager(PagingConfig(pageSize = 20)) {
            SearchPagingSource { page -> searchService.search(query, page) }
        }.flow.map { pagingData ->
            pagingData.map { searchResultDto ->
                searchResultDto.toDomain()
            }
        }
    }

    override fun getTrendingThisWeek(): Flow<PagingData<MultiResult>> {
        return Pager(PagingConfig(pageSize = 20)) {
            TrendingPagingSource { page -> trendingService.getTrending(TimeWindowDto.WEEK, page) }
        }.flow.map { pagingData ->
            pagingData.map { multiResultDto ->
                multiResultDto.toDomain()
            }
        }
    }
}

private fun MultiSearchResultDto.toDomain(): MultiResult {
    return when (this) {
        is MultiSearchResultDto.MovieDto -> toMovie()
        is MultiSearchResultDto.PersonDto -> toPerson()
        is MultiSearchResultDto.TvShowDto -> toTvShow()
    }
}