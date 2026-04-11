package core.database.account

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import core.database.account.model.FavoriteTvShowEntity
import core.database.account.model.FavoriteTvShowIdEntity

@Dao
interface FavoriteTvShowDao {

    @Query("SELECT * FROM FavoriteTvShowEntity WHERE page IS NOT NULL")
    fun getPagingSource(): PagingSource<Int, FavoriteTvShowEntity>

    @Query("SELECT * FROM FavoriteTvShowEntity WHERE page IS NOT NULL")
    suspend fun getAllTvShows(): List<FavoriteTvShowEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTvShows(tvShows: List<FavoriteTvShowEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg tvShow: FavoriteTvShowEntity)

    @Delete
    suspend fun delete(vararg tvShow: FavoriteTvShowEntity)

    @Query("DELETE FROM FavoriteTvShowEntity")
    suspend fun clearAllTvShows()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIds(vararg id: FavoriteTvShowIdEntity)

    @Delete
    suspend fun deleteIds(vararg id: FavoriteTvShowIdEntity)

    @Query("DELETE FROM FavoriteTvShowIdEntity")
    suspend fun clearAllIds()
}
