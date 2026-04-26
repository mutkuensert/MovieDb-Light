package feature.person.domain.model

data class PersonTvCredits(
    val cast: List<PersonTvCredit>,
    val crew: List<PersonTvCredit>,
)

data class PersonTvCredit(
    val id: Int,
    val title: String,
    val imagePath: String?,
    val voteAverage: Float?,
    val releaseDate: String?,
    val character: String?,
    val job: String?,
)
