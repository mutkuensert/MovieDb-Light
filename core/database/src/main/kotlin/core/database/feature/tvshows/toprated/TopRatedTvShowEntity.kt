package core.database.feature.tvshows.toprated

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import core.database.account.model.WatchlistTvShowIdEntity

@Entity
data class TopRatedTvShowEntity(
    val id: Int,
    val page: Int,
    val title: String,
    val posterPath: String?,
    val voteAverage: Float?,
) {
    @PrimaryKey(autoGenerate = true)
    var primaryKey: Int = 0
}

data class TopRatedTvShow(
    @Embedded val tvShow: TopRatedTvShowEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id"
    )
    val watchlistEntity: WatchlistTvShowIdEntity?
) {
    val inWatchlist get() = watchlistEntity != null
}