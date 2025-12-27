package feature.search.data

import core.data.DEFAULT_REMOTE_CONTENT_LANGUAGE
import core.data.network.NetworkResult
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {
    @GET("search/multi")
    suspend fun search(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("language") language: String = DEFAULT_REMOTE_CONTENT_LANGUAGE,
    ): NetworkResult<SearchResponse>
}