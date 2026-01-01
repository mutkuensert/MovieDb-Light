package feature.movie.presentation.detail.model

data class MovieUiModel(
    val id: Int,
    val title: String,
    val imagePath: String?,
    val voteAverage: String?,
    val inWatchlist: Boolean?
)