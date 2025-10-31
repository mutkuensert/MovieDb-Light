package core.database.feature.movies.popular

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface PopularMovieDao {

    @Transaction
    @Query("SELECT * FROM PopularMovieEntity")
    fun getPagingSource(): PagingSource<Int, PopularMovie>

    @Transaction
    @Query("SELECT * FROM PopularMovieEntity")
    fun getAll(): List<PopularMovie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movies: List<PopularMovieEntity>)

    @Query("DELETE FROM PopularMovieEntity")
    fun clearAll()
}