package feature.search.presentation

data class SearchUiModel(
    val query: String,
    val showSearchResult: Boolean,
) {
    companion object {
        fun initial(): SearchUiModel {
            return SearchUiModel(query = "", showSearchResult = false)
        }
    }
}
