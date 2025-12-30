package feature.movie.domain.model

data class MovieDetails(
    val imageUrl: String?,
    val title: String?,
    val voteAverage: Float?,
    val runtime: Int?,
    val releaseDate: String?,
    val overview: String?
)
