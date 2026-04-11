package feature.person.domain.model

data class PersonDetails(
    val id: Int,
    val name: String,
    val imagePath: String?,
    val biography: String?,
    val birthday: String?,
    val deathday: String?,
    val placeOfBirth: String?,
    val knownForDepartment: String?,
)
