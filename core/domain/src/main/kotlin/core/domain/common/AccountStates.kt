package core.domain.common

data class AccountStates(
    val id: Int,
    val favorite: Boolean,
    val rate: Float?,
    val watchlist: Boolean
)