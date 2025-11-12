package core.data.account.model

import core.domain.account.SortBy
import kotlinx.serialization.Serializable

object SortBySto {
    @Serializable
    enum class CreatedAtDto(val value: String) {
        ASCENDING("created_at.asc"), DESCENDING("created_at.desc")
    }
}

fun SortBy.CreatedAt.toDto(): SortBySto.CreatedAtDto {
    return if (this == SortBy.CreatedAt.ASCENDING) {
        SortBySto.CreatedAtDto.ASCENDING
    } else {
        SortBySto.CreatedAtDto.DESCENDING
    }
}
