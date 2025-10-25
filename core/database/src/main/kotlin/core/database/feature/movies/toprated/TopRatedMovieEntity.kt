package core.database.feature.movies.toprated

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TopRatedMovieEntity(
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