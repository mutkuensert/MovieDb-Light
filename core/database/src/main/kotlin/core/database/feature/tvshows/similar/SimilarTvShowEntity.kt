package core.database.feature.tvshows.similar

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import core.database.account.model.WatchlistTvShowIdEntity

@Entity
data class SimilarTvShowEntity(
    val id: Int,
    val page: Int,
    val title: String,
    val posterPath: String?,
    val voteAverage: Float?
) {
    @PrimaryKey(autoGenerate = true)
    var primaryKey: Int = 0
}

data class SimilarTvShow(
    @Embedded val tvShow: SimilarTvShowEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id"
    )
    val watchlistEntity: WatchlistTvShowIdEntity?
) {
    val inWatchlist get() = watchlistEntity != null
}
