package core.database.feature.movies.popular

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface PopularMovieDao {

    @Transaction
    @Query("SELECT * FROM PopularMovieEntity")
    fun getPagingSource(): PagingSource<Int, PopularMovie>

    @Transaction
    @Query("SELECT * FROM PopularMovieEntity")
    fun getAll(): List<PopularMovie>

    @Transaction
    @Query("SELECT * FROM PopularMovieEntity WHERE id = :id")
    suspend fun get(id: Int): PopularMovie?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movies: List<PopularMovieEntity>)

    @Update
    fun update(movie: PopularMovieEntity)

    @Delete
    fun delete(vararg movie: PopularMovieEntity)

    @Query("DELETE FROM PopularMovieEntity")
    fun clearAll()
}