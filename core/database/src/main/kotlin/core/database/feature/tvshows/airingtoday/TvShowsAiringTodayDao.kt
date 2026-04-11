package core.database.feature.tvshows.airingtoday

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface TvShowsAiringTodayDao {

    @Transaction
    @Query("SELECT * FROM TvShowAiringTodayEntity")
    fun getPagingSource(): PagingSource<Int, TvShowAiringToday>

    @Transaction
    @Query("SELECT * FROM TvShowAiringTodayEntity")
    suspend fun getAll(): List<TvShowAiringToday>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tvShows: List<TvShowAiringTodayEntity>)

    @Query("DELETE FROM TvShowAiringTodayEntity")
    suspend fun clearAll()
}