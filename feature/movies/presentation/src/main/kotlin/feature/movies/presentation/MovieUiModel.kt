package feature.movies.presentation

data class MovieUiModel(
    val id: Int,
    val title: String,
    val imageUrl: String?,
    val voteAverage: Double
)