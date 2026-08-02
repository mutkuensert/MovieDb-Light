package feature.search.presentation

sealed interface ResultUiModel {
    val id: Int
    val title: String
    val imagePath: String?

    interface Production {
        val voteAverage: String?
    }

    class Movie(
        override val id: Int,
        override val title: String,
        override val imagePath: String?,
        override val voteAverage: String?,
    ) : ResultUiModel, Production

    class TvShow(
        override val id: Int,
        override val title: String,
        override val imagePath: String?,
        override val voteAverage: String?,
    ) : ResultUiModel, Production

    class Person(
        override val id: Int,
        override val title: String,
        override val imagePath: String?,
    ) : ResultUiModel
}
