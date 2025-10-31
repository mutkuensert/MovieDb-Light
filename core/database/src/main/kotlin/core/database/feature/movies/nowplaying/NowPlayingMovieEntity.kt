package core.database.feature.movies.nowplaying

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import core.database.account.model.FavoriteMovieEntity

@Entity
data class NowPlayingMovieEntity(
    val id: Int,
    val page: Int,
    val title: String,
    val posterPath: String?,
    val voteAverage: Float
) {
    @PrimaryKey(autoGenerate = true)
    var primaryKey: Int = 0
}

data class NowPlayingMovie(
    @Embedded val movie: NowPlayingMovieEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id"
    )
    val favorite: FavoriteMovieEntity?
) {
    val isFavorite get() = favorite != null
}