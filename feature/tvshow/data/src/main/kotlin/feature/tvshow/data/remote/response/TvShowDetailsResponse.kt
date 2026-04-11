package feature.tvshow.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TvShowDetailsResponse(
    val adult: Boolean?,
    @SerialName("backdrop_path") val backdropPath: String?,
    @SerialName("created_by") val createdBy: List<TvShowCreatorDto>?,
    @SerialName("episode_run_time") val episodeRunTime: List<Int>?,
    @SerialName("first_air_date") val firstAirDate: String?,
    val genres: List<TvShowGenreDto>?,
    val homepage: String?,
    val id: Int?,
    @SerialName("in_production") val inProduction: Boolean?,
    @SerialName("last_air_date") val lastAirDate: String?,
    val name: String?,
    @SerialName("original_language") val originalLanguage: String?,
    @SerialName("original_name") val originalName: String?,
    val overview: String?,
    val popularity: Double?,
    @SerialName("poster_path") val posterPath: String?,
    @SerialName("production_companies") val productionCompanies: List<TvShowProductionCompanyDto>?,
    @SerialName("production_countries") val productionCountries: List<TvShowProductionCountryDto>?,
    @SerialName("number_of_episodes") val numberOfEpisodes: Int?,
    @SerialName("number_of_seasons") val numberOfSeasons: Int?,
    @SerialName("spoken_languages") val spokenLanguages: List<TvShowSpokenLanguageDto>?,
    val status: String?,
    val tagline: String?,
    val type: String?,
    @SerialName("vote_average") val voteAverage: Float?,
    @SerialName("vote_count") val voteCount: Int?
)

@Serializable
data class TvShowCreatorDto(
    val id: Int,
    @SerialName("credit_id") val creditId: String?,
    val name: String,
    val gender: Int?,
    @SerialName("profile_path") val profilePath: String?
)

@Serializable
data class TvShowGenreDto(
    val id: Int?,
    val name: String?
)

@Serializable
data class TvShowProductionCompanyDto(
    val id: Int,
    @SerialName("logo_path") val logoPath: String?,
    val name: String,
    @SerialName("origin_country") val originCountry: String?
)


@Serializable
data class TvShowProductionCountryDto(
    val iso_3166_1: String?,
    val name: String?
)

@Serializable
data class TvShowSpokenLanguageDto(
    val iso_639_1: String?,
    val name: String?
)
