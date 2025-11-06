package core.data.model.common

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.floatOrNull
import kotlinx.serialization.json.jsonPrimitive

@Serializable
data class AccountStatesResponse(
    val id: Int,
    val favorite: Boolean,
    @Serializable(with = RatedFieldSerializer::class)
    val rated: RatedDto?,
    val watchlist: Boolean
)

@Serializable
data class RatedDto(val value: Float?)

object RatedFieldSerializer : KSerializer<RatedDto?> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("RatedField")

    @OptIn(ExperimentalSerializationApi::class)
    override fun serialize(encoder: Encoder, value: RatedDto?) {
        if (value == null) {
            encoder.encodeNull()
        } else {
            encoder.encodeSerializableValue(JsonObject.serializer(), buildJsonObject {
                put("value", JsonPrimitive(value.value))
            })
        }
    }

    override fun deserialize(decoder: Decoder): RatedDto? {
        val element = decoder as? JsonDecoder ?: error("Expected JsonDecoder")
        return when (val jsonElement = element.decodeJsonElement()) {
            is JsonObject -> RatedDto(jsonElement["value"]?.jsonPrimitive?.floatOrNull)
            is JsonPrimitive -> if (jsonElement.booleanOrNull == false) null else null
            else -> null
        }
    }
}