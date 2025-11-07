package core.database.feature.movies.similar

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import core.database.account.model.WatchlistMovieIdEntity

@Entity
data class SimilarMovieEntity(
    val id: Int,
    val page: Int,
    val title: String,
    val posterPath: String?,
    val voteAverage: Float?
) {
    @PrimaryKey(autoGenerate = true)
    var primaryKey: Int = 0
}

data class SimilarMovie(
    @Embedded val movie: SimilarMovieEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id"
    )
    val watchlistEntity: WatchlistMovieIdEntity?
) {
    val inWatchlist get() = watchlistEntity != null
}
