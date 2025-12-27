package feature.search.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import feature.search.domain.SearchRepository
import feature.search.domain.SearchResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchRepositoryImpl(private val searchService: SearchService) : SearchRepository {
    override fun search(query: String): Flow<PagingData<SearchResult>> {
        return Pager(PagingConfig(pageSize = 20)) {
            SearchPagingSource { page ->
                searchService.search(query, page)
            }
        }.flow.map { pagingData ->
            pagingData.map { searchResultDto ->
                when (searchResultDto) {
                    is SearchResultDto.MovieDto -> searchResultDto.toMovie()
                    is SearchResultDto.PersonDto -> searchResultDto.toPerson()
                    is SearchResultDto.TvShowDto -> searchResultDto.toTvShow()
                }
            }
        }
    }
}