package feature.movie.presentation.detail

import feature.movie.domain.Person

data class PersonUiModel(
    val id: Int,
    val imageUrl: String?,
    val name: String,
    val character: String,
)

fun Person.toUiModel(): PersonUiModel {
    return PersonUiModel(id = id, imageUrl = imageUrl, name = name, character = character ?: "")
}