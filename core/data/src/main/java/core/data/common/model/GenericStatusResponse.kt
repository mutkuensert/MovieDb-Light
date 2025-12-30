package core.data.common.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenericStatusResponse(
    @SerialName("status_code")
    val statusCode: Int,
    @SerialName("status_message")
    val statusMessage: String
)