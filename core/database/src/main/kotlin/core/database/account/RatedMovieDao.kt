package core.database.account

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import core.database.account.model.RatedMovieEntity

@Dao
interface RatedMovieDao {

    @Query("SELECT * FROM RatedMovieEntity")
    fun getPagingSource(): PagingSource<Int, RatedMovieEntity>

    @Query("SELECT * FROM RatedMovieEntity")
    fun getAllMovies(): List<RatedMovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movies: List<RatedMovieEntity>)

    @Query("DELETE FROM RatedMovieEntity WHERE id = :movieId")
    fun delete(movieId: Int)

    @Query("DELETE FROM RatedMovieEntity")
    fun clearAllMovies()
}