package core.database.feature.movies.upcoming

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import core.database.account.model.WatchlistMovieIdEntity

@Entity
data class UpcomingMovieEntity(
    val id: Int,
    val page: Int,
    val title: String,
    val posterPath: String?,
    val voteAverage: Float?
) {
    @PrimaryKey(autoGenerate = true)
    var primaryKey: Int = 0
}

data class UpcomingMovie(
    @Embedded val movie: UpcomingMovieEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id"
    )
    val watchlistEntity: WatchlistMovieIdEntity?
) {
    val inWatchlist get() = watchlistEntity != null
}