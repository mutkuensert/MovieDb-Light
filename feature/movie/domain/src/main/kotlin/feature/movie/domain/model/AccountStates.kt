package feature.movie.domain.model

data class AccountStates(
    val id: Int,
    val favorite: Boolean,
    val rate: Float?,
    val watchlist: Boolean
)