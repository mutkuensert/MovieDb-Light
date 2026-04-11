package feature.person.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PersonDetailsResponse(
    val id: Int,
    val name: String,
    @SerialName("profile_path") val profilePath: String?,
    val biography: String?,
    val birthday: String?,
    val deathday: String?,
    @SerialName("place_of_birth") val placeOfBirth: String?,
    @SerialName("known_for_department") val knownForDepartment: String?,
)
