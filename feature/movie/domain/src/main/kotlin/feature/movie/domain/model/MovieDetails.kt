package feature.movie.domain.model

data class MovieDetails(
    val imagePath: String?,
    val title: String?,
    val voteAverage: Float?,
    val runtime: Int?,
    val releaseDate: String?,
    val genres: List<String>,
    val overview: String?,
)
