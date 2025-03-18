package core.database.feature.movies.nowplaying

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface NowPlayingMovieDao {

    @Query("SELECT * FROM NowPlayingMovieEntity")
    fun getPagingSource(): PagingSource<Int, NowPlayingMovieEntity>

    @Query("SELECT * FROM NowPlayingMovieEntity")
    fun getAll(): List<NowPlayingMovieEntity>

    @Query("SELECT * FROM NowPlayingMovieEntity WHERE id = :id")
    suspend fun get(id: Int): NowPlayingMovieEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movies: List<NowPlayingMovieEntity>)

    @Update
    fun update(movie: NowPlayingMovieEntity)

    @Delete
    fun delete(vararg movie: NowPlayingMovieEntity)

    @Query("DELETE FROM NowPlayingMovieEntity")
    fun clearAll()
}