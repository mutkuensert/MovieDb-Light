package feature.person.presentation.detail.model

import core.ui.TmdbImage
import feature.person.domain.model.PersonMovieCredit

data class MovieUiModel(
    val id: Int,
    val title: String,
    val imagePath: String?,
    val voteAverage: String?,
    val year: String,
    val character: String,
    val job: String,
)

fun PersonMovieCredit.toUiModel(): MovieUiModel {
    return MovieUiModel(
        id = id,
        title = title,
        imagePath = imagePath?.let { TmdbImage(it) }?.poster?.w500Url,
        voteAverage = voteAverage?.toString(),
        year = releaseDate?.split("-")?.firstOrNull() ?: "",
        character = character ?: "",
        job = job ?: ""
    )
}
