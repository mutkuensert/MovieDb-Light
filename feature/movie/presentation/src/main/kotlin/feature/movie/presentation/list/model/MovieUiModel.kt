package feature.movie.presentation.list.model

data class MovieUiModel(
    val id: Int,
    val title: String,
    val imagePath: String?,
    val voteAverage: String?,
)
