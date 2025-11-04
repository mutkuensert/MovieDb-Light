package feature.movie.domain

data class Movie(
    val id: Int,
    val title: String,
    val imageUrl: String?,
    val voteAverage: Float?,
    val inWatchlist: Boolean?,
)