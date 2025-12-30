package feature.movie.presentation.detail.model

data class MovieUiModel(
    val id: Int,
    val title: String,
    val imageUrl: String?,
    val voteAverage: String?,
    val inWatchlist: Boolean?
)