package feature.movie.presentation.detail

data class MovieDetailUiModel(
    val id: Int,
    val imageUrl: String?,
    val title: String,
    val vote: String,
    val showRateButton: Boolean,
    val userRate: String?,
    val inWatchlist: Boolean?,
    val favorite: Boolean?,
    val runtime: String,
    val year: String,
    val overview: String,
    val trailerUrl: String?,
    val providerLogoUrls: List<String>,
    val cast: List<PersonUiModel>
) {
    companion object {
        fun initial(id: Int): MovieDetailUiModel {
            return MovieDetailUiModel(
                id = id,
                imageUrl = null,
                title = "",
                vote = "",
                showRateButton = false,
                userRate = null,
                inWatchlist = null,
                favorite = null,
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
