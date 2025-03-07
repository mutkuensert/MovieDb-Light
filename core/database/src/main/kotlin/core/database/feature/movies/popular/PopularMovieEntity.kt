package core.database.feature.movies.popular

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class PopularMovieEntity(
    val id: Int,
    val page: Int,
    val title: String,
    @ColumnInfo(name = "poster_path") val posterPath: String?,
    @ColumnInfo(name = "vote_average") val voteAverage: Double
) {
    @PrimaryKey(autoGenerate = true)
    var primaryKey: Int = 0
}