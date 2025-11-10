package core.database.account

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import core.database.account.model.WatchlistMovieEntity
import core.database.account.model.WatchlistMovieIdEntity

@Dao
interface WatchlistMovieDao {

    @Query("SELECT * FROM WatchlistMovieEntity")
    fun getPagingSource(): PagingSource<Int, WatchlistMovieEntity>

    @Query("SELECT * FROM WatchlistMovieEntity")
    fun getAllMovies(): List<WatchlistMovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movies: List<WatchlistMovieEntity>)

    @Query("DELETE FROM WatchlistMovieEntity WHERE id = :movieId")
    fun deleteMovie(movieId: Int)

    @Query("DELETE FROM WatchlistMovieEntity")
    fun clearAllMovies()


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIds(vararg movie: WatchlistMovieIdEntity)

    @Delete
    suspend fun deleteIds(vararg movie: WatchlistMovieIdEntity)

    @Query("DELETE FROM WatchlistMovieIdEntity")
    suspend fun clearAllIds()
}