package core.data.account

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AccountDetailsResponse(
    val avatar: AvatarDto,
    val id: Int,
    @SerialName("include_adult") val includeAdult: Boolean,
    val iso_3166_1: String,
    val iso_639_1: String,
    val name: String,
    val username: String
)

@Serializable
data class AvatarDto(
    val gravatar: GravatarDto,
    val tmdb: TmdbDto
)

@Serializable
data class GravatarDto(
    val hash: String?
)

@Serializable
data class TmdbDto(
    @SerialName("avatar_path") val avatarPath: String?
)