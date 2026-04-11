package core.database.feature.tvshows.upcoming

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface UpcomingTvShowDao {

    @Transaction
    @Query("SELECT * FROM UpcomingTvShowEntity")
    fun getPagingSource(): PagingSource<Int, UpcomingTvShow>

    @Transaction
    @Query("SELECT * FROM UpcomingTvShowEntity")
    suspend fun getAll(): List<UpcomingTvShow>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tvShows: List<UpcomingTvShowEntity>)

    @Query("DELETE FROM UpcomingTvShowEntity")
    suspend fun clearAll()
}