package feature.tvshow.presentation.list.model

data class TvShowUiModel(
    val id: Int,
    val title: String,
    val imagePath: String?,
    val voteAverage: String?,
    val inWatchlist: Boolean?
)