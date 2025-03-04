package feature.movies.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PopularMoviesResponse(
    val page: Int,
    val results: List<PopularMovieDto>,
    @SerialName("total_results") val totalResults: Int,
    @SerialName("total_pages") val totalPages: Int
)

@Serializable
data class PopularMovieDto(
    val id: Int,
    val title: String,
    @SerialName("poster_path") val posterPath: String?,
    @SerialName("vote_average") val voteAverage: Double
)