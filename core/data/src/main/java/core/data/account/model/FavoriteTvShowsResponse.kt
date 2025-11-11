package core.data.account.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FavoriteTvShowsResponse(
    val page: Int,
    val results: List<FavoriteTvShowsResultsDto>,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("total_results")
    val totalResults: Int
)

@Serializable
data class FavoriteTvShowsResultsDto(
    val adult: Boolean,
    @SerialName("backdrop_path") val backdropPath: String,
    @SerialName("genre_ids") val genreIds: List<Int>,
    val id: Int,
    @SerialName("original_language") val originalLanguage: String,
    @SerialName("original_name") val originalName: String,
    val overview: String,
    val popularity: Double,
    @SerialName("poster_path") val posterPath: String,
    @SerialName("first_air_date") val firstAitDate: String,
    val name: String,
    @SerialName("vote_average") val voteAverage: Double,
    @SerialName("vote_count") val voteCount: Int
)

@Serializable
data class FavoriteTvShowDto(
    val favorite: Boolean,
    @SerialName("media_id") val mediaId: Int,
    @SerialName("media_type")
    val mediaType: String = "tv"
)