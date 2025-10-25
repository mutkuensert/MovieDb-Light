package core.database.account.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteTvShowEntity(
    @PrimaryKey val id: Int
)