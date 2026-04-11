package core.database.account

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import core.database.account.model.WatchlistTvShowEntity
import core.database.account.model.WatchlistTvShowIdEntity

@Dao
interface WatchlistTvShowDao {

    @Query("SELECT * FROM WatchlistTvShowEntity")
    fun getPagingSource(): PagingSource<Int, WatchlistTvShowEntity>

    @Query("SELECT * FROM WatchlistTvShowEntity")
    suspend fun getAll(): List<WatchlistTvShowEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTvShows(tvShows: List<WatchlistTvShowEntity>)

    @Query("DELETE FROM WatchlistTvShowEntity")
    suspend fun clearAllTvShows()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIds(vararg tvShow: WatchlistTvShowIdEntity)

    @Delete
    suspend fun deleteIds(vararg tvShow: WatchlistTvShowIdEntity)

    @Query("DELETE FROM WatchlistTvShowIdEntity")
    suspend fun clearAllIds()
}
