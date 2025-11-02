package feature.movie.presentation.list

data class MovieUiModel(
    val id: Int,
    val title: String,
    val imageUrl: String?,
    val voteAverage: String,
    val isFavorite: Boolean?
)