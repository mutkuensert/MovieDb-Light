package core.database.feature.movies.nowplaying

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface NowPlayingMovieDao {

    @Transaction
    @Query("SELECT * FROM NowPlayingMovieEntity")
    fun getPagingSource(): PagingSource<Int, NowPlayingMovie>

    @Transaction
    @Query("SELECT * FROM NowPlayingMovieEntity")
    fun getAll(): List<NowPlayingMovie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movies: List<NowPlayingMovieEntity>)

    @Query("DELETE FROM NowPlayingMovieEntity")
    fun clearAll()
}