package core.data.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewSessionRequest(
    @SerialName("request_token") val validRequestToken: String
)
