package core.database.feature.movies.toprated

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface TopRatedMovieDao {

    @Transaction
    @Query("SELECT * FROM TopRatedMovieEntity")
    fun getPagingSource(): PagingSource<Int, TopRatedMovieRelations>

    @Transaction
    @Query("SELECT * FROM TopRatedMovieEntity")
    fun getAll(): List<TopRatedMovieRelations>

    @Transaction
    @Query("SELECT * FROM TopRatedMovieEntity WHERE id = :id")
    suspend fun get(id: Int): TopRatedMovieRelations?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movies: List<TopRatedMovieEntity>)

    @Update
    fun update(movie: TopRatedMovieEntity)

    @Delete
    fun delete(vararg movie: TopRatedMovieEntity)

    @Query("DELETE FROM TopRatedMovieEntity")
    fun clearAll()
}