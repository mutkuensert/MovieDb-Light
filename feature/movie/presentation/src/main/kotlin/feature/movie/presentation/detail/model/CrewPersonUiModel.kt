package feature.movie.presentation.detail.model

import feature.movie.domain.model.Director
import feature.movie.domain.model.Writer

data class CrewPersonUiModel(
    val id: Int,
    val name: String,
)

fun Director.toUiModel(): CrewPersonUiModel {
    return CrewPersonUiModel(id = id, name = name)
}

fun Writer.toUiModel(): CrewPersonUiModel {
    return CrewPersonUiModel(id = id, name = name)
}
