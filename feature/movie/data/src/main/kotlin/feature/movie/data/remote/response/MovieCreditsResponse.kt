package feature.movie.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieCreditsResponse(
    val id: Int,
    val cast: List<MovieCastDto>
)

@Serializable
data class MovieCastDto(
    val id: Int,
    val name: String,
    @SerialName("profile_path") val profilePath: String?,
    val character: String?
)