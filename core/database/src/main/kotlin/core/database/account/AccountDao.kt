package core.database.account

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import core.database.account.model.FavoriteMovieIdEntity
import core.database.account.model.FavoriteTvShowEntity
import core.database.account.model.WatchlistMovieIdEntity

@Dao
interface AccountDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteMovieIds(vararg favoriteMovie: FavoriteMovieIdEntity)

    @Delete
    suspend fun deleteFavoriteMovieIds(vararg favoriteMovie: FavoriteMovieIdEntity)

    @Query("DELETE FROM FavoriteMovieIdEntity")
    suspend fun clearAllFavoriteMovieIds()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWatchlistMovieIds(vararg movie: WatchlistMovieIdEntity)

    @Delete
    suspend fun deleteWatchlistMovieIds(vararg movie: WatchlistMovieIdEntity)

    @Query("DELETE FROM WatchlistMovieIdEntity")
    suspend fun clearAllWatchlistMovieIds()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteTvShows(vararg favoriteTvShow: FavoriteTvShowEntity)

    @Delete
    suspend fun deleteFavoriteTvShows(vararg favoriteTvShow: FavoriteTvShowEntity)

    @Query("DELETE FROM FavoriteTvShowEntity")
    suspend fun clearAllFavoriteTvShows()
}