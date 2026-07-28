package feature.movie.presentation.detail.model

import core.domain.common.model.Review
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

data class MovieDetailUiModel(
    val id: Int,
    val imagePaths: List<String>,
    val title: String,
    val vote: String,
    val showFavoriteButton: Boolean,
    val showWatchlistButton: Boolean,
    val showRateButton: Boolean,
    val userRate: String?,
    val inWatchlist: Boolean?,
    val favorite: Boolean?,
    val runtime: String,
    val releaseDate: String,
    val genres: String,
    val overview: String,
    val youtubeVideoId: String?,
    val providerLogoPaths: List<String>,
    val cast: List<PersonUiModel>,
    val directors: List<CrewPersonUiModel>,
    val writers: List<CrewPersonUiModel>,
    val review: ReviewUiModel?,
    val showsReviewsButton: Boolean,
) {
    companion object {
        fun initial(id: Int): MovieDetailUiModel {
            return MovieDetailUiModel(
                id = id,
                imagePaths = emptyList(),
                title = "",
                vote = "",
                showFavoriteButton = false,
                showWatchlistButton = false,
                showRateButton = false,
                userRate = null,
                inWatchlist = null,
                favorite = null,
                runtime = "",
                releaseDate = "",
                genres = "",
                overview = "",
                youtubeVideoId = null,
                providerLogoPaths = emptyList(),
                cast = emptyList(),
                directors = emptyList(),
                writers = emptyList(),
                review = null,
                showsReviewsButton = false,
            )
        }
    }
}

data class ReviewUiModel(
    val id: String,
    val author: String,
    val content: String,
    val editedAt: String,
    val rating: String?,
)

fun Review.toUiModel(): ReviewUiModel {
    val dateTime = DateTimeFormatter
        .ofPattern("dd.MM.yyyy HH:mm")
        .withZone(ZoneOffset.UTC)
        .format(editedAt ?: createdAt)

    return ReviewUiModel(
        id = id,
        author = author,
        content = content,
        editedAt = dateTime,
        rating = rating?.toString(),
    )
}
