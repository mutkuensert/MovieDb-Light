package feature.movie.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NowPlayingMoviesResponse(
    val page: Int,
    val results: List<NowPlayingMovieDto>
)

@Serializable
data class NowPlayingMovieDto(
    val id: Int,
    val title: String,
    @SerialName("poster_path") val posterPath: String?,
    @SerialName("vote_average") val voteAverage: Float
)
