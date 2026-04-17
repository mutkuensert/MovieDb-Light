package core.database.feature.tvshows.popular

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import core.database.account.model.WatchlistTvShowIdEntity

@Entity
data class PopularTvShowEntity(
    val id: Int,
    val page: Int,
    val title: String,
    val posterPath: String?,
    val voteAverage: Float?
) {
    @PrimaryKey(autoGenerate = true)
    var primaryKey: Int = 0
}

data class PopularTvShow(
    @Embedded val tvShow: PopularTvShowEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id"
    )
    val watchlistEntity: WatchlistTvShowIdEntity?
) {
    val inWatchlist get() = watchlistEntity != null
}