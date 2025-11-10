package core.database.account

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import core.database.account.model.FavoriteTvShowEntity

@Dao
interface FavoriteTvShowDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg tvShow: FavoriteTvShowEntity)

    @Delete
    suspend fun delete(vararg tvShow: FavoriteTvShowEntity)

    @Query("DELETE FROM FavoriteTvShowEntity")
    suspend fun clearAll()
}