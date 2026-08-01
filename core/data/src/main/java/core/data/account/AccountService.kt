package core.data.account

import core.data.account.model.AccountDetailsResponse
import core.data.account.model.FavoriteMovieRequest
import core.data.account.model.FavoriteTvShowDto
import core.data.account.model.PostFavoriteTvShowResponse
import core.data.account.model.SortBySto
import core.data.account.model.WatchlistMovieRequest
import core.data.account.model.WatchlistTvShowRequest
import core.data.common.model.GenericStatusResponse
import core.data.common.model.MoviesResponse
import core.data.common.model.TvShowsResponse
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
        @Query("sort_by") sortBy: String = SortBySto.CreatedAtDto.ASCENDING.value,
        @Query("language") language: String = "en-US",
    ): NetworkResult<MoviesResponse>

    @POST("account/account_id/favorite")
    suspend fun postFavoriteMovie(
        @Body favoriteMovieRequest: FavoriteMovieRequest,
        @Query("session_id") sessionId: String,
    ): NetworkResult<GenericStatusResponse>

    @GET("account/account_id/watchlist/movies")
    suspend fun getWatchlistMovies(
        @Query("page") page: Int = 1,
        @Query("session_id") sessionId: String,
        @Query("sort_by") sortBy: String = SortBySto.CreatedAtDto.ASCENDING.value,
        @Query("language") language: String = "en-US",
    ): NetworkResult<MoviesResponse>

    @POST("account/account_id/watchlist")
    suspend fun postWatchlistMovie(
        @Body watchlistMovieRequest: WatchlistMovieRequest,
        @Query("session_id") sessionId: String,
    ): NetworkResult<GenericStatusResponse>

    @POST("account/account_id/watchlist")
    suspend fun postWatchlistTvShow(
        @Body watchlistTvShowRequest: WatchlistTvShowRequest,
        @Query("session_id") sessionId: String,
    ): NetworkResult<GenericStatusResponse>

    @GET("account/account_id/favorite/tv")
    suspend fun getFavoriteTvShows(
        @Query("page") page: Int = 1,
        @Query("session_id") sessionId: String,
        @Query("sort_by") sortBy: String = SortBySto.CreatedAtDto.ASCENDING.value,
        @Query("language") language: String = "en-US",
    ): NetworkResult<TvShowsResponse>

    @GET("account/account_id/watchlist/tv")
    suspend fun getWatchlistTvShows(
        @Query("page") page: Int = 1,
        @Query("session_id") sessionId: String,
        @Query("sort_by") sortBy: String = SortBySto.CreatedAtDto.ASCENDING.value,
        @Query("language") language: String = "en-US",
    ): NetworkResult<TvShowsResponse>

    @GET("account/account_id/rated/tv")
    suspend fun getRatedTvShows(
        @Query("page") page: Int = 1,
        @Query("session_id") sessionId: String,
        @Query("sort_by") sortBy: String = SortBySto.CreatedAtDto.ASCENDING.value,
        @Query("language") language: String = "en-US",
    ): NetworkResult<TvShowsResponse>

    @POST("account/account_id/favorite")
    suspend fun postFavoriteTvShow(
        @Body favoriteTvShowDto: FavoriteTvShowDto,
        @Query("session_id") sessionId: String,
    ): NetworkResult<PostFavoriteTvShowResponse>

    @GET("account/account_id/rated/movies")
    suspend fun getRatedMovies(
        @Query("page") page: Int = 1,
        @Query("session_id") sessionId: String,
        @Query("sort_by") sortBy: String = SortBySto.CreatedAtDto.ASCENDING.value,
        @Query("language") language: String = "en-US",
    ): NetworkResult<MoviesResponse>
}
