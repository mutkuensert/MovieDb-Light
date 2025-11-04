package core.database.account

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import core.database.account.model.FavoriteMovieEntity
import core.database.account.model.FavoriteTvShowEntity
import core.database.account.model.WatchlistMovieEntity

@Dao
interface AccountDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteMovies(vararg favoriteMovie: FavoriteMovieEntity)

    @Delete
    suspend fun deleteFavoriteMovies(vararg favoriteMovie: FavoriteMovieEntity)

    @Query("DELETE FROM FavoriteMovieEntity")
    suspend fun clearAllFavoriteMovies()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWatchlistMovies(vararg movie: WatchlistMovieEntity)

    @Delete
    suspend fun deleteWatchlistMovies(vararg movie: WatchlistMovieEntity)

    @Query("DELETE FROM WatchlistMovieEntity")
    suspend fun clearAllWatchlistMovies()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteTvShows(vararg favoriteTvShow: FavoriteTvShowEntity)

    @Delete
    suspend fun deleteFavoriteTvShows(vararg favoriteTvShow: FavoriteTvShowEntity)

    @Query("DELETE FROM FavoriteTvShowEntity")
    suspend fun clearAllFavoriteTvShows()
}