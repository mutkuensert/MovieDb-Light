package feature.person.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PersonTvCreditsResponse(
    val cast: List<PersonTvCastDto>?,
    val crew: List<PersonTvCrewDto>?
)

@Serializable
data class PersonTvCastDto(
    val adult: Boolean?,
    @SerialName("backdrop_path")
    val backdropPath: String?,
    @SerialName("genre_ids")
    val genreIds: List<Int>?,
    val id: Int,
    @SerialName("origin_country")
    val originCountry: List<String>?,
    @SerialName("original_language")
    val originalLanguage: String?,
    @SerialName("original_name")
    val originalName: String?,
    val overview: String?,
    val popularity: Double?,
    @SerialName("poster_path")
    val posterPath: String?,
    @SerialName("first_air_date")
    val firstAirDate: String?,
    val name: String?,
    @SerialName("vote_average")
    val voteAverage: Float?,
    @SerialName("vote_count")
    val voteCount: Int?,
    val character: String?,
    @SerialName("credit_id")
    val creditId: String?,
    @SerialName("episode_count")
    val episodeCount: Int?
)

@Serializable
data class PersonTvCrewDto(
    val adult: Boolean?,
    @SerialName("backdrop_path")
    val backdropPath: String?,
    @SerialName("genre_ids")
    val genreIds: List<Int>?,
    val id: Int,
    @SerialName("origin_country")
    val originCountry: List<String>?,
    @SerialName("original_language")
    val originalLanguage: String?,
    @SerialName("original_name")
    val originalName: String?,
    val overview: String?,
    val popularity: Double?,
    @SerialName("poster_path")
    val posterPath: String?,
    @SerialName("first_air_date")
    val firstAirDate: String?,
    val name: String?,
    @SerialName("vote_average")
    val voteAverage: Float?,
    @SerialName("vote_count")
    val voteCount: Int?,
    val character: String?,
    @SerialName("credit_id")
    val creditId: String?,
    @SerialName("episode_count")
    val episodeCount: Int?,
    val department: String?,
    val job: String?
)