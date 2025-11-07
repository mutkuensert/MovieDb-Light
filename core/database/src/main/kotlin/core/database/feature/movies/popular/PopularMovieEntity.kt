package core.database.feature.movies.popular

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import core.database.account.model.WatchlistMovieIdEntity
import core.database.feature.movies.nowplaying.NowPlayingMovieEntity

@Entity
data class PopularMovieEntity(
    val id: Int,
    val page: Int,
    val title: String,
    val posterPath: String?,
    val voteAverage: Float?
) {
    @PrimaryKey(autoGenerate = true)
    var primaryKey: Int = 0
}

data class PopularMovie(
    @Embedded val movie: NowPlayingMovieEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id"
    )
    val watchlistEntity: WatchlistMovieIdEntity?
) {
    val inWatchlist get() = watchlistEntity != null
}