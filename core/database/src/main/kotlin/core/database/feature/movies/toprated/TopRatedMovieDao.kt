package core.database.feature.movies.toprated

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface TopRatedMovieDao {

    @Transaction
    @Query("SELECT * FROM TopRatedMovieEntity")
    fun getPagingSource(): PagingSource<Int, TopRatedMovie>

    @Transaction
    @Query("SELECT * FROM TopRatedMovieEntity")
    fun getAll(): List<TopRatedMovie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movies: List<TopRatedMovieEntity>)

    @Query("DELETE FROM TopRatedMovieEntity")
    fun clearAll()
}