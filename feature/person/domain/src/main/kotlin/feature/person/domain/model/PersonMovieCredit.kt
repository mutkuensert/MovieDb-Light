package feature.person.domain.model

data class PersonMovieCredits(
    val cast: List<PersonMovieCredit>,
    val crew: List<PersonMovieCredit>,
)

data class PersonMovieCredit(
    val id: Int,
    val title: String,
    val imagePath: String?,
    val voteAverage: Float?,
    val releaseDate: String?,
    val character: String?,
    val job: String?,
)
