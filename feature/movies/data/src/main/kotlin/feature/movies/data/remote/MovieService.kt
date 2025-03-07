package feature.movies.data.remote

import core.data.network.NetworkResult
import feature.movies.data.remote.response.PopularMoviesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {

    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("page") page: Int): NetworkResult<PopularMoviesResponse>

    /*@GET("movie/now_playing")
    suspend fun getMoviesNowPlaying(@Query("page") page: Int): NetworkResult<MoviesNowPlayingResponse>*/
}