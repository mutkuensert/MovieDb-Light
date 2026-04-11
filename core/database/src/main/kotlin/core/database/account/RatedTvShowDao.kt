package core.database.account

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import core.database.account.model.RatedTvShowEntity

@Dao
interface RatedTvShowDao {

    @Query("SELECT * FROM RatedTvShowEntity")
    fun getPagingSource(): PagingSource<Int, RatedTvShowEntity>

    @Query("SELECT * FROM RatedTvShowEntity")
    fun getAll(): List<RatedTvShowEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTvShows(tvShows: List<RatedTvShowEntity>)

    @Query("DELETE FROM RatedTvShowEntity")
    fun clearAll()
}
