package feature.movie.presentation.detail.model

import feature.movie.domain.model.Person

data class PersonUiModel(
    val id: Int,
    val imagePath: String?,
    val name: String,
    val character: String,
)

fun Person.toUiModel(): PersonUiModel {
    return PersonUiModel(id = id, imagePath = imagePath, name = name, character = character ?: "")
}