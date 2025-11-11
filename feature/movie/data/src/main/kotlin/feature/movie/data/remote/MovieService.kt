package feature.movie.data.remote

import core.data.model.common.AccountStatesResponse
import core.data.model.common.GenericStatusResponse
import core.data.model.common.MoviesResponse
import core.data.network.NetworkResult
import feature.movie.data.remote.response.MovieCreditsResponse
import feature.movie.data.remote.response.MovieDetailsResponse
import feature.movie.data.remote.response.MovieVideosResponse
import feature.movie.data.remote.response.PostMovieRatingRequest
import feature.movie.data.remote.response.ProvidersResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int,
        @Query("region") countryCode: String? = null,
        @Query("language") language: String = "en-US",
    ): NetworkResult<MoviesResponse>

    @GET("movie/now_playing")
    suspend fun getMoviesNowPlaying(
        @Query("page") page: Int,
        @Query("region") countryCode: String? = null,
        @Query("language") language: String = "en-US",
    ): NetworkResult<MoviesResponse>

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("page") page: Int,
        @Query("region") countryCode: String? = null,
        @Query("language") language: String = "en-US",
    ): NetworkResult<MoviesResponse>

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("page") page: Int,
        @Query("region") countryCode: String? = null,
        @Query("language") language: String = "en-US",
    ): NetworkResult<MoviesResponse>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String = "en-US",
    ): NetworkResult<MovieDetailsResponse>

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String = "en-US",
    ): NetworkResult<MovieCreditsResponse>

    @GET("movie/{movie_id}/watch/providers")
    suspend fun getProviders(
        @Path("movie_id") movieId: Int
    ): NetworkResult<ProvidersResponse>

    @GET("movie/{movie_id}/similar")
    suspend fun getSimilarMovies(
        @Path("movie_id") movieId: Int,
        @Query("page") page: Int,
        @Query("language") language: String = "en-US",
    ): NetworkResult<MoviesResponse>

    @GET("movie/{movie_id}/videos")
    suspend fun getVideos(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String = "en-US",
    ): NetworkResult<MovieVideosResponse>

    @GET("movie/{movie_id}/account_states")
    suspend fun getAccountStates(
        @Path("movie_id") movieId: Int,
        @Query("session_id") sessionId: String,
    ): NetworkResult<AccountStatesResponse>

    @POST("movie/{movie_id}/rating")
    suspend fun rateMovie(
        @Path("movie_id") movieId: Int,
        @Body request: PostMovieRatingRequest,
        @Query("session_id") sessionId: String,
    ): NetworkResult<GenericStatusResponse>

    @DELETE("movie/{movie_id}/rating")
    suspend fun deleteRating(
        @Path("movie_id") movieId: Int,
        @Query("session_id") sessionId: String,
    ): NetworkResult<GenericStatusResponse>
}