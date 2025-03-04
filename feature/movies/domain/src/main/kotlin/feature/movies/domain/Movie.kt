package feature.movies.domain

data class Movie(
    val id: Int,
    val title: String,
    val imageUrl: String?,
    val isFavorite: Boolean?,
    val voteAverage: Double
)