package core.data.account

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WatchlistMovieRequest(
    val watchlist: Boolean,
    @SerialName("media_id") val mediaId: Int,
) {
    @Suppress("UNUSED")
    @SerialName("media_type")
    val mediaType: String = "movie"
}