package feature.movie.domain

data class MovieDetails(
    val imageUrl: String?,
    val title: String?,
    val voteAverage: Float?,
    val runtime: Int?,
    val overview: String?
)
