package feature.tvshow.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class PostTvShowRatingRequest(
    val value: Int?
)
