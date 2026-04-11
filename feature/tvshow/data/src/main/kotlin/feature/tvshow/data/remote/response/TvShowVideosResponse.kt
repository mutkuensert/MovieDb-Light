package feature.tvshow.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TvShowVideosResponse(
    val id: Int,
    val results: List<TvShowVideoDto>,
)

@Serializable
data class TvShowVideoDto(
    @SerialName("iso_639_1")
    val iso639_1: String,
    @SerialName("iso_3166_1")
    val iso3166_1: String,
    val name: String,
    val key: String,
    val site: String,
    val size: Int,
    val type: String,
    val official: Boolean,
    @SerialName("published_at")
    val publishedAt: String,
    val id: String
)
