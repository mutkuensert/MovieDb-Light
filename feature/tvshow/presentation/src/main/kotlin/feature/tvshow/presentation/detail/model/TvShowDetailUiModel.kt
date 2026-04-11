package feature.tvshow.presentation.detail.model

data class TvShowDetailUiModel(
    val id: Int,
    val imagePaths: List<String>,
    val title: String,
    val vote: String,
    val showFavoriteButton: Boolean,
    val showWatchlistButton: Boolean,
    val showRateButton: Boolean,
    val userRate: String?,
    val inWatchlist: Boolean?,
    val favorite: Boolean?,
    val runtime: String,
    val year: String,
    val genres: String,
    val overview: String,
    val trailerUrl: String?,
    val providerLogoPaths: List<String>,
    val cast: List<PersonUiModel>,
) {
    companion object {
        fun initial(id: Int): TvShowDetailUiModel {
            return TvShowDetailUiModel(
                id = id,
                imagePaths = emptyList(),
                title = "",
                vote = "",
                showFavoriteButton = false,
                showWatchlistButton = false,
                showRateButton = false,
                userRate = null,
                inWatchlist = null,
                favorite = null,
                runtime = "",
                year = "",
                genres = "",
                overview = "",
                trailerUrl = null,
                providerLogoPaths = emptyList(),
                cast = emptyList(),
            )
        }
    }
}
