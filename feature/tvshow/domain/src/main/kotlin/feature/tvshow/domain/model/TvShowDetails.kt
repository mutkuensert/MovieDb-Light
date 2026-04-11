package feature.tvshow.domain.model

data class TvShowDetails(
    val imagePath: String?,
    val title: String?,
    val voteAverage: Float?,
    val runtime: Int?,
    val releaseDate: String?,
    val genres: List<String>,
    val overview: String?,
)
