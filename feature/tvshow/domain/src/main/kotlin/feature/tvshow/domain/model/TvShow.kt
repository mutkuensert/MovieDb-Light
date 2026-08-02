package feature.tvshow.domain.model

data class TvShow(
    val id: Int,
    val title: String,
    val imagePath: String?,
    val voteAverage: Float?,
)
