package feature.search.domain

sealed interface MultiResult {
    val id: Int
    val imagePath: String?

    class Movie(
        override val id: Int,
        val title: String?,
        override val imagePath: String?
    ) : MultiResult

    class TvShow(
        override val id: Int,
        val title: String?,
        override val imagePath: String?
    ) : MultiResult

    class Person(
        override val id: Int,
        val name: String?,
        override val imagePath: String?
    ) : MultiResult
}