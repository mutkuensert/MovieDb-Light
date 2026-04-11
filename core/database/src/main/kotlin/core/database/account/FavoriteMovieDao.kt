package core.database.account

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import core.database.account.model.FavoriteMovieEntity
import core.database.account.model.FavoriteMovieIdEntity

@Dao
interface FavoriteMovieDao {

    @Query("SELECT * FROM FavoriteMovieEntity")
    fun getPagingSource(): PagingSource<Int, FavoriteMovieEntity>

    @Query("SELECT * FROM FavoriteMovieEntity")
    suspend fun getAllMovies(): List<FavoriteMovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<FavoriteMovieEntity>)

    @Query("DELETE FROM FavoriteMovieEntity WHERE id = :movieId")
    suspend fun deleteMovie(movieId: Int)

    @Query("DELETE FROM FavoriteMovieEntity")
    suspend fun clearAllMovies()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIds(vararg id: FavoriteMovieIdEntity)

    @Delete
    suspend fun deleteIds(vararg id: FavoriteMovieIdEntity)

    @Query("DELETE FROM FavoriteMovieIdEntity")
    suspend fun clearAllIds()
}