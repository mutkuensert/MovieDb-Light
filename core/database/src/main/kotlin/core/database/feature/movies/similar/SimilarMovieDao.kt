package core.database.feature.movies.similar

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface SimilarMovieDao {

    @Transaction
    @Query("SELECT * FROM SimilarMovieEntity")
    fun getPagingSource(): PagingSource<Int, SimilarMovie>

    @Transaction
    @Query("SELECT * FROM SimilarMovieEntity")
    fun getAll(): List<SimilarMovie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movies: List<SimilarMovieEntity>)

    @Query("DELETE FROM SimilarMovieEntity")
    fun clearAll()
}