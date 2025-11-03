package feature.movie.presentation.detail

data class MovieDetailUiModel(
    val imageUrl: String?,
    val title: String,
    val voteAverage: String,
    val runtime: String,
    val releaseDate: String,
    val overview: String,
    val cast: List<PersonUiModel>
) {
    companion object {
        fun initial(): MovieDetailUiModel {
            return MovieDetailUiModel(
                imageUrl = null,
                title = "",
                voteAverage = "",
                runtime = "",
                releaseDate = "",
                overview = "",
                cast = emptyList()
            )
        }
    }
}
