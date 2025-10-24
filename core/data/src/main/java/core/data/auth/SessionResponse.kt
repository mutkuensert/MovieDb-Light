package core.data.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SessionResponse(
    val success: Boolean,
    @SerialName("session_id") val sessionId: String
)