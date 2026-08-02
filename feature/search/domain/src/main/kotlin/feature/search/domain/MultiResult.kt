package feature.search.domain

sealed interface MultiResult {
    val id: Int
    val title: String?
    val imagePath: String?
    val voteAverage: Float?

    class Movie(
        override val id: Int,
        override val title: String?,
        override val imagePath: String?,
        override val voteAverage: Float?,
    ) : MultiResult

    class TvShow(
        override val id: Int,
        override val title: String?,
        override val imagePath: String?,
        override val voteAverage: Float?,
    ) : MultiResult

    class Person(
        override val id: Int,
        override val title: String?,
        override val imagePath: String?,
        override val voteAverage: Float? = null,
    ) : MultiResult
}
