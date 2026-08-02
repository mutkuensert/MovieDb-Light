package feature.profile.domain.model

data class Production(
    val id: Int,
    val title: String,
    val imagePath: String?,
    val voteAverage: Float?,
)