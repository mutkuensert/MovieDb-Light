package feature.movie.domain.model

data class Movie(
    val id: Int,
    val title: String,
    val imagePath: String?,
    val voteAverage: Float?,
)
