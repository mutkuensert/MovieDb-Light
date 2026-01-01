package feature.profile.presentation.profile

import core.domain.account.SortBy

data class ProfileUiModel(
    val profileImagePath: String?,
    val name: String,
    val favoriteMoviesSortBy: SortByUiModel,
    val watchlistMoviesSortBy: SortByUiModel,
    val ratedMoviesSortBy: SortByUiModel,

    ) {
    companion object {
        fun empty(): ProfileUiModel {
            return ProfileUiModel(
                profileImagePath = null,
                name = "",
                favoriteMoviesSortBy = SortByUiModel.ASCENDING,
                watchlistMoviesSortBy = SortByUiModel.ASCENDING,
                ratedMoviesSortBy = SortByUiModel.ASCENDING,
            )
        }
    }
}

enum class SortByUiModel {
    ASCENDING, DESCENDING
}

fun SortByUiModel.toDomain(): SortBy.CreatedAt {
    return when (this) {
        SortByUiModel.ASCENDING -> SortBy.CreatedAt.ASCENDING
        SortByUiModel.DESCENDING -> SortBy.CreatedAt.DESCENDING
    }
}