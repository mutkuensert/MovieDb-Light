package core.database.feature.tvshows.popular

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface PopularTvShowDao {

    @Transaction
    @Query("SELECT * FROM PopularTvShowEntity")
    fun getPagingSource(): PagingSource<Int, PopularTvShow>

    @Transaction
    @Query("SELECT * FROM PopularTvShowEntity")
    suspend fun getAll(): List<PopularTvShow>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tvShows: List<PopularTvShowEntity>)

    @Query("DELETE FROM PopularTvShowEntity")
    suspend fun clearAll()
}