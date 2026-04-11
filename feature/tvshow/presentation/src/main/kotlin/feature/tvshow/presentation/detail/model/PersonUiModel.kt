package feature.tvshow.presentation.detail.model

import feature.tvshow.domain.model.Person

data class PersonUiModel(
    val id: Int,
    val imagePath: String?,
    val name: String,
    val character: String,
)

fun Person.toUiModel(): PersonUiModel {
    return PersonUiModel(id = id, imagePath = imagePath, name = name, character = character ?: "")
}