package core.data.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SessionIdRequest(
    @SerialName("session_id") val sessionId: String
)
