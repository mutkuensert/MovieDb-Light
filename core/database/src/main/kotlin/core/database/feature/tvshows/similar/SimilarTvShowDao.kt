package core.database.feature.tvshows.similar

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface SimilarTvShowDao {

    @Transaction
    @Query("SELECT * FROM SimilarTvShowEntity")
    fun getPagingSource(): PagingSource<Int, SimilarTvShow>

    @Transaction
    @Query("SELECT * FROM SimilarTvShowEntity")
    suspend fun getAll(): List<SimilarTvShow>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tvShows: List<SimilarTvShowEntity>)

    @Query("DELETE FROM SimilarTvShowEntity")
    suspend fun clearAll()
}