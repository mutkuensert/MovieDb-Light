package feature.movie.presentation.list.model

data class MoviesUiModel(
    val selectedCountry: String?,
    val isCountryDialogVisible: Boolean
) {
    companion object {
        fun initial(): MoviesUiModel {
            return MoviesUiModel(
                selectedCountry = null,
                isCountryDialogVisible = false,
            )
        }
    }
}