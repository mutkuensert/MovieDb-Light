package core.domain

data class User(
    val id: Int,
    val name: String,
    val userName: String,
    val profilePicturePath: String?,
    val includeAdult: Boolean,
)
