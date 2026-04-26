package feature.tvshow.data.remote

import core.data.DEFAULT_REMOTE_CONTENT_LANGUAGE
import core.data.common.model.AccountStatesResponse
import core.data.common.model.GenericStatusResponse
import core.data.common.model.ReviewsResponse
import core.data.common.model.TvShowsResponse
import core.data.network.NetworkResult
import feature.tvshow.data.remote.response.ImagesResponse
import feature.tvshow.data.remote.response.TvShowCreditsResponse
import feature.tvshow.data.remote.response.TvShowDetailsResponse
import feature.tvshow.data.remote.response.TvShowVideosResponse
import feature.tvshow.data.remote.response.PostTvShowRatingRequest
import feature.tvshow.data.remote.response.ProvidersResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface TvShowService {

    @GET("tv/popular")
    suspend fun getPopularTvShows(
        @Query("page") page: Int,
        @Query("region") countryCode: String? = null,
        @Query("language") language: String = DEFAULT_REMOTE_CONTENT_LANGUAGE,
    ): NetworkResult<TvShowsResponse>

    @GET("tv/airing_today")
    suspend fun getTvShowsAiringToday(
        @Query("page") page: Int,
        @Query("region") countryCode: String? = null,
        @Query("language") language: String = DEFAULT_REMOTE_CONTENT_LANGUAGE,
    ): NetworkResult<TvShowsResponse>

    @GET("tv/top_rated")
    suspend fun getTopRatedTvShows(
        @Query("page") page: Int,
        @Query("region") countryCode: String? = null,
        @Query("language") language: String = DEFAULT_REMOTE_CONTENT_LANGUAGE,
    ): NetworkResult<TvShowsResponse>

    @GET("tv/on_the_air")
    suspend fun getUpcomingTvShows(
        @Query("page") page: Int,
        @Query("region") countryCode: String? = null,
        @Query("language") language: String = DEFAULT_REMOTE_CONTENT_LANGUAGE,
    ): NetworkResult<TvShowsResponse>

    @GET("tv/{tv_id}")
    suspend fun getTvShowDetails(
        @Path("tv_id") tvShowId: Int,
        @Query("language") language: String = DEFAULT_REMOTE_CONTENT_LANGUAGE,
    ): NetworkResult<TvShowDetailsResponse>

    @GET("tv/{tv_id}/credits")
    suspend fun getTvShowCredits(
        @Path("tv_id") tvShowId: Int,
        @Query("language") language: String = DEFAULT_REMOTE_CONTENT_LANGUAGE,
    ): NetworkResult<TvShowCreditsResponse>

    @GET("tv/{tv_id}/watch/providers")
    suspend fun getProviders(
        @Path("tv_id") tvShowId: Int
    ): NetworkResult<ProvidersResponse>

    @GET("tv/{tv_id}/similar")
    suspend fun getSimilarTvShows(
        @Path("tv_id") tvShowId: Int,
        @Query("page") page: Int,
        @Query("language") language: String = DEFAULT_REMOTE_CONTENT_LANGUAGE,
    ): NetworkResult<TvShowsResponse>

    @GET("tv/{tv_id}/images")
    suspend fun getImages(
        @Path("tv_id") tvShowId: Int,
        @Query("include_image_language") includeLanguage: List<String>? = null,
        @Query("language") language: String = DEFAULT_REMOTE_CONTENT_LANGUAGE,
    ): NetworkResult<ImagesResponse>

    @GET("tv/{tv_id}/videos")
    suspend fun getVideos(
        @Path("tv_id") tvShowId: Int,
        @Query("language") language: String = DEFAULT_REMOTE_CONTENT_LANGUAGE,
    ): NetworkResult<TvShowVideosResponse>

    @GET("tv/{tv_id}/reviews")
    suspend fun getReviews(
        @Path("tv_id") tvShowId: Int,
        @Query("language") language: String = DEFAULT_REMOTE_CONTENT_LANGUAGE,
        @Query("page") page: Int = 1,
    ): NetworkResult<ReviewsResponse>

    @GET("tv/{tv_id}/account_states")
    suspend fun getAccountStates(
        @Path("tv_id") tvShowId: Int,
        @Query("session_id") sessionId: String,
    ): NetworkResult<AccountStatesResponse>

    @POST("tv/{tv_id}/rating")
    suspend fun rateTvShow(
        @Path("tv_id") tvShowId: Int,
        @Body request: PostTvShowRatingRequest,
        @Query("session_id") sessionId: String,
    ): NetworkResult<GenericStatusResponse>

    @DELETE("tv/{tv_id}/rating")
    suspend fun deleteRating(
        @Path("tv_id") tvShowId: Int,
        @Query("session_id") sessionId: String,
    ): NetworkResult<GenericStatusResponse>
}
