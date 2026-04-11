package feature.tvshow.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TvShowCreditsResponse(
    val id: Int,
    val cast: List<TvShowCastDto>,
    val crew: List<TvShowCrewDto>
)

@Serializable
data class TvShowCastDto(
    val id: Int,
    val name: String,
    @SerialName("profile_path") val profilePath: String?,
    val character: String?
)

@Serializable
data class TvShowCrewDto(
    val id: Int,
    val name: String,
    @SerialName("profile_path") val profilePath: String?,
    val job: String?
)