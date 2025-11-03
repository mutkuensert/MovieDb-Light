package feature.movie.domain

data class MovieDetailsAndCast(
    val imageUrl: String?,
    val title: String?,
    val voteAverage: Float?,
    val runtime: Int?,
    val releaseDate: String?,
    val overview: String?,
    val cast: List<Person>,
)
