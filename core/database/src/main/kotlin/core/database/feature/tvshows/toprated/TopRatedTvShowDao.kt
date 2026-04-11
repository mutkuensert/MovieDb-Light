package core.database.feature.tvshows.toprated

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface TopRatedTvShowDao {

    @Transaction
    @Query("SELECT * FROM TopRatedTvShowEntity")
    fun getPagingSource(): PagingSource<Int, TopRatedTvShow>

    @Transaction
    @Query("SELECT * FROM TopRatedTvShowEntity")
    suspend fun getAll(): List<TopRatedTvShow>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tvShows: List<TopRatedTvShowEntity>)

    @Query("DELETE FROM TopRatedTvShowEntity")
    suspend fun clearAll()
}