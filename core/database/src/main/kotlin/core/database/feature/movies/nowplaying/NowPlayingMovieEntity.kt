package core.database.feature.movies.nowplaying

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NowPlayingMovieEntity(
    val id: Int,
    val page: Int,
    val title: String,
    val posterPath: String?,
    val voteAverage: Float,
    val isFavorite: Boolean?
) {
    @PrimaryKey(autoGenerate = true)
    var primaryKey: Int = 0
}