package feature.movie.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class PostMovieRatingRequest(
    val value: Int?
)
