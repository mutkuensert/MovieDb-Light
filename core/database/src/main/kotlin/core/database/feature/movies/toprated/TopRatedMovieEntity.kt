package core.database.feature.movies.toprated

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import core.database.account.model.WatchlistMovieIdEntity

@Entity
data class TopRatedMovieEntity(
    val id: Int,
    val page: Int,
    val title: String,
    val posterPath: String?,
    val voteAverage: Float?,
) {
    @PrimaryKey(autoGenerate = true)
    var primaryKey: Int = 0
}

data class TopRatedMovie(
    @Embedded val movie: TopRatedMovieEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id"
    )
    val watchlistEntity: WatchlistMovieIdEntity?
) {
    val inWatchlist get() = watchlistEntity != null
}