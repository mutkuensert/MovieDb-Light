package core.database.account.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteMovieEntity(
    val id: Int,
    val page: Int,
    val title: String,
    val posterPath: String?,
    val voteAverage: Float?
) {
    @PrimaryKey(autoGenerate = true)
    var primaryKey: Int = 0
}
