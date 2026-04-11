package core.database.account.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteTvShowIdEntity(
    @PrimaryKey val id: Int
)
