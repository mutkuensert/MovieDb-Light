package core.database.feature.tvshows.airingtoday

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import core.database.account.model.WatchlistTvShowIdEntity

@Entity
data class TvShowAiringTodayEntity(
    val id: Int,
    val page: Int,
    val title: String,
    val posterPath: String?,
    val voteAverage: Float?
) {
    @PrimaryKey(autoGenerate = true)
    var primaryKey: Int = 0
}

data class TvShowAiringToday(
    @Embedded val tvShow: TvShowAiringTodayEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id"
    )
    val watchlistEntity: WatchlistTvShowIdEntity?
) {
    val inWatchlist get() = watchlistEntity != null
}