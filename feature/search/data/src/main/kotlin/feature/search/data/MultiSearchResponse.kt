package feature.search.data

import feature.search.domain.MultiResult
import kotlinx.serialization.KSerializer
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Serializable
data class MultiSearchResponse(
    val page: Int,
    val results: List<MultiSearchResultDto>
)

@Serializable(with = MultiSearchResultDtoSerializer::class)
sealed interface MultiSearchResultDto {
    @SerialName("media_type")
    val mediaType: String

    @Serializable
    data class MovieDto(
        val adult: Boolean?,
        @SerialName("backdrop_path")
        val backdropPath: String?,
        val id: Int,
        val title: String?,
        @SerialName("original_language")
        val originalLanguage: String?,
        @SerialName("original_title")
        val originalTitle: String?,
        val overview: String?,
        @SerialName("poster_path")
        val posterPath: String?,
        @SerialName("genre_ids")
        val genreIds: List<Int>?,
        val popularity: Float?,
        @SerialName("release_date")
        val releaseDate: String?,
        val video: Boolean?,
        @SerialName("vote_average")
        val voteAverage: Float?,
        @SerialName("vote_count")
        val voteCount: Int?,
        @SerialName("media_type") override val mediaType: String
    ) : MultiSearchResultDto {
        fun toMovie(): MultiResult.Movie {
            return MultiResult.Movie(id, title, posterPath)
        }
    }

    @Serializable
    data class TvShowDto(
        val adult: Boolean?,
        @SerialName("backdrop_path")
        val backdropPath: String?,
        val id: Int,
        val name: String?,
        @SerialName("original_language")
        val originalLanguage: String?,
        @SerialName("original_name")
        val originalName: String?,
        val overview: String?,
        @SerialName("poster_path")
        val posterPath: String?,
        @SerialName("genre_ids")
        val genreIds: List<Int>?,
        val popularity: Float?,
        @SerialName("first_air_date")
        val firstAirDate: String?,
        @SerialName("vote_average")
        val voteAverage: Float?,
        @SerialName("vote_count")
        val voteCount: Int?,
        @SerialName("origin_country")
        val originCountry: List<String>,
        @SerialName("media_type") override val mediaType: String,
    ) : MultiSearchResultDto {
        fun toTvShow(): MultiResult.TvShow {
            return MultiResult.TvShow(id, name, posterPath)
        }
    }

    @Serializable
    data class PersonDto(
        val adult: Boolean?,
        val id: Int,
        val name: String?,
        @SerialName("original_name")
        val originalName: String?,
        val popularity: Float?,
        val gender: Int?,
        @SerialName("known_for_department")
        val knownForDepartment: String?,
        @SerialName("profile_path")
        val profilePath: String?,
        @SerialName("known_for")
        val knownFor: List<MultiSearchResultDto>?,
        @SerialName("media_type") override val mediaType: String
    ) : MultiSearchResultDto {
        fun toPerson(): MultiResult.Person {
            return MultiResult.Person(
                id,
                name,
                profilePath
            )
        }
    }
}

private object MediaType {
    const val MOVIE = "movie"
    const val TV_SHOW = "tv"
    const val PERSON = "person"
}

object MultiSearchResultDtoSerializer : KSerializer<MultiSearchResultDto> {
    val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
        prettyPrint = true
        coerceInputValues = true
    }
    override val descriptor: SerialDescriptor
        get() = PolymorphicSerializer(MultiSearchResultDto::class).descriptor

    override fun serialize(encoder: Encoder, value: MultiSearchResultDto) {
        when (value) {
            is MultiSearchResultDto.MovieDto -> {
                encoder.encodeSerializableValue(MultiSearchResultDto.MovieDto.serializer(), value)
            }

            is MultiSearchResultDto.TvShowDto -> {
                encoder.encodeSerializableValue(MultiSearchResultDto.TvShowDto.serializer(), value)
            }

            is MultiSearchResultDto.PersonDto -> {
                encoder.encodeSerializableValue(MultiSearchResultDto.PersonDto.serializer(), value)
            }
        }
    }

    override fun deserialize(decoder: Decoder): MultiSearchResultDto {
        val jsonElement = (decoder as JsonDecoder).decodeJsonElement()
        return when (val itemType =
            jsonElement.jsonObject["media_type"]?.jsonPrimitive?.content) {
            MediaType.MOVIE -> {
                json.decodeFromJsonElement(MultiSearchResultDto.MovieDto.serializer(), jsonElement)
            }

            MediaType.TV_SHOW -> {
                json.decodeFromJsonElement(MultiSearchResultDto.TvShowDto.serializer(), jsonElement)
            }

            MediaType.PERSON -> {
                json.decodeFromJsonElement(MultiSearchResultDto.PersonDto.serializer(), jsonElement)
            }

            else -> throw SerializationException("Unknown itemType: $itemType")
        }
    }
}