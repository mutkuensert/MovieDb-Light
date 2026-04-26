package feature.person.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PersonMovieCreditsResponse(
    val id: Int,
    val cast: List<PersonMovieCastDto>?,
    val crew: List<PersonMovieCrewDto>?,
)

@Serializable
data class PersonMovieCastDto(
    val id: Int,
    val title: String?,
    @SerialName("original_title") val originalTitle: String?,
    @SerialName("poster_path") val posterPath: String?,
    @SerialName("vote_average") val voteAverage: Float?,
    @SerialName("release_date") val releaseDate: String?,
    val character: String?,
    val popularity: Float?,
)

@Serializable
data class PersonMovieCrewDto(
    val id: Int,
    val title: String?,
    @SerialName("original_title") val originalTitle: String?,
    @SerialName("poster_path") val posterPath: String?,
    @SerialName("vote_average") val voteAverage: Float?,
    @SerialName("release_date") val releaseDate: String?,
    val job: String?,
    val popularity: Float?,
)
