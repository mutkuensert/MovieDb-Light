package core.data.common.model

import core.data.util.InstantSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class ReviewsResponse(
    val id: Int,
    val page: Int?,
    val results: List<ReviewDto>?,
    @SerialName("total_pages")
    val totalPages: Int?,
    @SerialName("total_results")
    val totalResults: Int?
)

@Serializable
data class ReviewDto(
    val author: String?,
    @SerialName("author_details")
    val authorDetails: AuthorDetailsDto?,
    val content: String?,
    @SerialName("created_at")
    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant?,
    val id: String,
    @SerialName("updated_at")
    @Serializable(with = InstantSerializer::class)
    val updatedAt: Instant?,
    val url: String?
)

@Serializable
data class AuthorDetailsDto(
    val name: String?,
    val username: String?,
    @SerialName("avatar_path")
    val avatarPath: String?,
    val rating: Double?
)
