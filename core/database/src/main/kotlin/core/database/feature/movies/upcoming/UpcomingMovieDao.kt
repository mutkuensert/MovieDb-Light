package core.database.feature.movies.upcoming

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface UpcomingMovieDao {

    @Transaction
    @Query("SELECT * FROM UpcomingMovieEntity")
    fun getPagingSource(): PagingSource<Int, UpcomingMovie>

    @Transaction
    @Query("SELECT * FROM UpcomingMovieEntity")
    fun getAll(): List<UpcomingMovie>

    @Transaction
    @Query("SELECT * FROM UpcomingMovieEntity WHERE id = :id")
    suspend fun get(id: Int): UpcomingMovie?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movies: List<UpcomingMovieEntity>)

    @Update
    fun update(movie: UpcomingMovieEntity)

    @Delete
    fun delete(vararg movie: UpcomingMovieEntity)

    @Query("DELETE FROM UpcomingMovieEntity")
    fun clearAll()
}