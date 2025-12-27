package feature.search.data

import feature.search.domain.SearchResult
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

data class SearchResponse(
    val page: Int,
    val results: List<SearchResultDto>
)

@Serializable(with = SearchResultDtoSerializer::class)
sealed interface SearchResultDto {
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
        override val mediaType: String
    ) : SearchResultDto {
        fun toMovie(): SearchResult.Movie {
            return SearchResult.Movie()
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
        override val mediaType: String,
    ) : SearchResultDto {
        fun toTvShow(): SearchResult.TvShow {
            return SearchResult.TvShow()
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
        val knownFor: List<SearchResultDto>?,
        override val mediaType: String
    ) : SearchResultDto {
        fun toPerson(): SearchResult.Person {
            return SearchResult.Person()
        }
    }
}

private object MediaType {
    const val MOVIE = "movie"
    const val TV_SHOW = "tv"
    const val PERSON = "person"
}

object SearchResultDtoSerializer : KSerializer<SearchResultDto> {
    val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
        prettyPrint = true
        coerceInputValues = true
    }
    override val descriptor: SerialDescriptor
        get() = PolymorphicSerializer(SearchResultDto::class).descriptor

    override fun serialize(encoder: Encoder, value: SearchResultDto) {
        when (value) {
            is SearchResultDto.MovieDto -> {
                encoder.encodeSerializableValue(SearchResultDto.MovieDto.serializer(), value)
            }

            is SearchResultDto.TvShowDto -> {
                encoder.encodeSerializableValue(SearchResultDto.TvShowDto.serializer(), value)
            }

            is SearchResultDto.PersonDto -> {
                encoder.encodeSerializableValue(SearchResultDto.PersonDto.serializer(), value)
            }
        }
    }

    override fun deserialize(decoder: Decoder): SearchResultDto {
        val jsonElement = (decoder as JsonDecoder).decodeJsonElement()
        return when (val itemType =
            jsonElement.jsonObject["media_type"]?.jsonPrimitive?.content) {
            MediaType.MOVIE -> {
                json.decodeFromJsonElement(SearchResultDto.MovieDto.serializer(), jsonElement)
            }

            MediaType.TV_SHOW -> {
                json.decodeFromJsonElement(SearchResultDto.TvShowDto.serializer(), jsonElement)
            }

            MediaType.PERSON -> {
                json.decodeFromJsonElement(SearchResultDto.PersonDto.serializer(), jsonElement)
            }

            else -> throw SerializationException("Unknown itemType: $itemType")
        }
    }
}