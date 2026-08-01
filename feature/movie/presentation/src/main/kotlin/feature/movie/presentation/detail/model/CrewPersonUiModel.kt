package feature.movie.presentation.detail.model

import android.os.Parcelable
import feature.movie.domain.model.Director
import feature.movie.domain.model.Writer
import kotlinx.parcelize.Parcelize

@Parcelize
data class CrewPersonUiModel(
    val id: Int,
    val name: String,
) : Parcelable

fun Director.toUiModel(): CrewPersonUiModel {
    return CrewPersonUiModel(id = id, name = name)
}

fun Writer.toUiModel(): CrewPersonUiModel {
    return CrewPersonUiModel(id = id, name = name)
}
