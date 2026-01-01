package feature.search.data

import core.data.DEFAULT_REMOTE_CONTENT_LANGUAGE
import core.data.network.NetworkResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TrendingService {
    @GET("trending/all/{time_window}")
    suspend fun getTrending(
        @Path("time_window") timeWindow: String,
        @Query("page") page: Int,
        @Query("language") language: String = DEFAULT_REMOTE_CONTENT_LANGUAGE,
    ): NetworkResult<MultiSearchResponse>
}