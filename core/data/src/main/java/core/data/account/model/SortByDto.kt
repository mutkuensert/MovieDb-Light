package core.data.account.model

import core.domain.account.SortBy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object SortByDto {

    @Serializable(CreatedAtSerializer::class)
    enum class CreatedAt(val value: String) {
        ASCENDING("created_at.asc"), DESCENDING("created_at.dsc")
    }
}

object CreatedAtSerializer : KSerializer<SortByDto.CreatedAt> {
    override val descriptor = PrimitiveSerialDescriptor("CreatedAt", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: SortByDto.CreatedAt) {
        encoder.encodeString(value.value)
    }

    override fun deserialize(decoder: Decoder): SortByDto.CreatedAt {
        val jsonValue = decoder.decodeString()
        return SortByDto.CreatedAt.entries.find { it.value == jsonValue }!!
    }
}

fun SortBy.CreatedAt.toDto(): SortByDto.CreatedAt {
    return if (this == SortBy.CreatedAt.ASCENDING) {
        SortByDto.CreatedAt.ASCENDING
    } else {
        SortByDto.CreatedAt.DESCENDING
    }
}