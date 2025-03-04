package core.database.feature.movies.popular

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface PopularMovieDao {

    @Query("SELECT * FROM PopularMovieEntity")
    fun getPagingSource(): PagingSource<Int, PopularMovieEntity>

    @Query("SELECT * FROM PopularMovieEntity")
    fun getAll(): List<PopularMovieEntity>

    @Query("SELECT * FROM PopularMovieEntity WHERE id = :id")
    suspend fun get(id: Int): PopularMovieEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movies: List<PopularMovieEntity>)

    @Update
    fun update(movie: PopularMovieEntity)

    @Delete
    fun delete(vararg movie: PopularMovieEntity)

    @Query("DELETE FROM PopularMovieEntity")
    fun clearAll()
}