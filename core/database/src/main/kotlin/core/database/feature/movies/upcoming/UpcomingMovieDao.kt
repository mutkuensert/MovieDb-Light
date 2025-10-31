package core.database.feature.movies.upcoming

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface UpcomingMovieDao {

    @Transaction
    @Query("SELECT * FROM UpcomingMovieEntity")
    fun getPagingSource(): PagingSource<Int, UpcomingMovie>

    @Transaction
    @Query("SELECT * FROM UpcomingMovieEntity")
    fun getAll(): List<UpcomingMovie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movies: List<UpcomingMovieEntity>)

    @Query("DELETE FROM UpcomingMovieEntity")
    fun clearAll()
}