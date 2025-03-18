package feature.movies.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpcomingMoviesResponse(
    val page: Int,
    val results: List<UpcomingMovieDto>
)

@Serializable
data class UpcomingMovieDto(
    val id: Int,
    val title: String,
    @SerialName("poster_path") val posterPath: String?,
    @SerialName("vote_average") val voteAverage: Float
)