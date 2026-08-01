package feature.person.presentation.detail.model

import android.os.Parcelable
import core.ui.TmdbImage
import feature.person.domain.model.PersonMovieCredit
import feature.person.domain.model.PersonTvCredit
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductionUiModel(
    val id: Int,
    val title: String,
    val imagePath: String?,
    val voteAverage: String?,
    val year: String,
    val character: String,
    val job: String,
) : Parcelable

fun PersonMovieCredit.toUiModel(): ProductionUiModel {
    return ProductionUiModel(
        id = id,
        title = title,
        imagePath = imagePath?.let { TmdbImage(it) }?.poster?.w500Url,
        voteAverage = voteAverage?.toString(),
        year = releaseDate?.split("-")?.firstOrNull() ?: "",
        character = character ?: "",
        job = job ?: ""
    )
}

fun PersonTvCredit.toUiModel(): ProductionUiModel {
    return ProductionUiModel(
        id = id,
        title = title,
        imagePath = imagePath?.let { TmdbImage(it) }?.poster?.w500Url,
        voteAverage = voteAverage?.toString(),
        year = releaseDate?.split("-")?.firstOrNull() ?: "",
        character = character ?: "",
        job = job ?: ""
    )
}
