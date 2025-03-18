package feature.movies.data.remote

import core.data.network.NetworkResult
import feature.movies.data.remote.response.NowPlayingMoviesResponse
import feature.movies.data.remote.response.PopularMoviesResponse
import feature.movies.data.remote.response.TopRatedMoviesResponse
import feature.movies.data.remote.response.UpcomingMoviesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int,
        @Query("region") countryCode: String? = null
    ): NetworkResult<PopularMoviesResponse>

    @GET("movie/now_playing")
    suspend fun getMoviesNowPlaying(
        @Query("page") page: Int,
        @Query("region") countryCode: String? = null
    ): NetworkResult<NowPlayingMoviesResponse>

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("page") page: Int,
        @Query("region") countryCode: String? = null
    ): NetworkResult<TopRatedMoviesResponse>

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("page") page: Int,
        @Query("region") countryCode: String? = null
    ): NetworkResult<UpcomingMoviesResponse>
}