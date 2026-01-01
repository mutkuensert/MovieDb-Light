package feature.search.presentation

sealed interface ResultUiModel {
    val id: Int
    val imagePath: String?

    class Movie(
        override val id: Int,
        override val imagePath: String?
    ) : ResultUiModel

    class TvShow(
        override val id: Int,
        override val imagePath: String?
    ) : ResultUiModel

    class Person(
        override val id: Int,
        override val imagePath: String?
    ) : ResultUiModel
}