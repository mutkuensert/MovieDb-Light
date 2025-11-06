package feature.movie.presentation.detail

data class MovieDetailUiModel(
    val imageUrl: String?,
    val title: String,
    val vote: String,
    val runtime: String,
    val year: String,
    val overview: String,
    val trailerUrl: String?,
    val providerLogoUrls: List<String>,
    val cast: List<PersonUiModel>
) {
    companion object {
        fun initial(): MovieDetailUiModel {
            return MovieDetailUiModel(
                imageUrl = null,
                title = "",
                vote = "",
                runtime = "",
                year = "",
                overview = "",
                trailerUrl = null,
                providerLogoUrls = emptyList(),
                cast = emptyList()
            )
        }
    }
}
