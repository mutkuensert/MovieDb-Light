package feature.search.domain

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun search(query: String): Flow<PagingData<MultiResult>>
    fun getTrendingThisWeek(): Flow<PagingData<MultiResult>>
}

