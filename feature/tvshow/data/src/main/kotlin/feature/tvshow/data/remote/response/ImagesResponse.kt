package feature.tvshow.data.remote.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImagesResponse(
    val backdrops: List<BackdropDto>?,
    val id: Int?,
    val logos: List<LogoDto>?,
    val posters: List<PosterDto>?
)

@Serializable
data class BackdropDto(
    @SerialName("aspect_ratio")
    val aspectRatio: Double?,
    @SerialName("file_path")
    val filePath: String,
    val height: Int?,
    @SerialName("iso_639_1")
    val iso6391: String?,
    @SerialName("vote_average")
    val voteAverage: Double?,
    @SerialName("vote_count")
    val voteCount: Int?,
    val width: Int?
)

@Serializable
data class LogoDto(
    @SerialName("aspect_ratio")
    val aspectRatio: Double?,
    @SerialName("file_path")
    val filePath: String,
    val height: Int?,
    @SerialName("iso_639_1")
    val iso6391: String?,
    @SerialName("vote_average")
    val voteAverage: Double?,
    @SerialName("vote_count")
    val voteCount: Int?,
    val width: Int?
)

@Serializable
data class PosterDto(
    @SerialName("aspect_ratio")
    val aspectRatio: Double?,
    @SerialName("file_path")
    val filePath: String,
    val height: Int?,
    @SerialName("iso_639_1")
    val iso6391: String?,
    @SerialName("vote_average")
    val voteAverage: Double?,
    @SerialName("vote_count")
    val voteCount: Int?,
    val width: Int?
)