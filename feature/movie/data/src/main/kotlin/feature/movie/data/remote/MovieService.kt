package feature.movie.data.remote

import core.data.network.NetworkResult
import feature.movie.data.remote.response.MovieCreditsResponse
import feature.movie.data.remote.response.MovieDetailsResponse
import feature.movie.data.remote.response.NowPlayingMoviesResponse
import feature.movie.data.remote.response.PopularMoviesResponse
import feature.movie.data.remote.response.TopRatedMoviesResponse
import feature.movie.data.remote.response.UpcomingMoviesResponse
import retrofit2.http.GET
import retrofit2.http.Path
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

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int
    ): NetworkResult<MovieDetailsResponse>

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(
        @Path("movie_id") movieId: Int
    ): NetworkResult<MovieCreditsResponse>
}