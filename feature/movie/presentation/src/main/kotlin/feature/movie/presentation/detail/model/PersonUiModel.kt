package feature.movie.presentation.detail.model

import android.os.Parcelable
import feature.movie.domain.model.Person
import kotlinx.parcelize.Parcelize

@Parcelize
data class PersonUiModel(
    val id: Int,
    val imagePath: String?,
    val name: String,
    val character: String,
) : Parcelable

fun Person.toUiModel(): PersonUiModel {
    return PersonUiModel(id = id, imagePath = imagePath, name = name, character = character ?: "")
}