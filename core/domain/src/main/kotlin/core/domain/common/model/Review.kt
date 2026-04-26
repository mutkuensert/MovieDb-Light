package core.domain.common.model

import java.time.Instant

data class Review(
    val id: String,
    val author: String,
    val content: String,
    val createdAt: Instant,
    val editedAt: Instant?,
    val rating: Double?,
)
