package core.data.account

import core.data.network.NetworkResult
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AccountService {

    @GET("account")
    suspend fun getAccountDetails(
        @Query("session_id") sessionId: String
    ): NetworkResult<AccountDetailsResponse>

    @GET("account/account_id/favorite/movies")
    suspend fun getFavoriteMovies(
        @Query("page") page: Int = 1,
        @Query("session_id") sessionId: String,
    ): NetworkResult<FavoriteMoviesResponse>

    @POST("account/account_id/favorite")
    suspend fun postFavoriteMovie(
        @Body favoriteMovieDto: FavoriteMovieDto,
        @Query("session_id") sessionId: String,
    ): NetworkResult<PostFavoriteMovieResponse>

    @GET("account/account_id/favorite/tv")
    suspend fun getFavoriteTvShows(
        @Query("page") page: Int = 1,
        @Query("session_id") sessionId: String,
    ): NetworkResult<FavoriteTvShowsResponse>

    @POST("account/account_id/favorite")
    suspend fun postFavoriteTvShow(
        @Body favoriteTvShowDto: FavoriteTvShowDto,
        @Query("session_id") sessionId: String,
    ): NetworkResult<PostFavoriteTvShowResponse>
}