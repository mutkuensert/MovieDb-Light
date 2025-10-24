package core.data.auth

import kotlinx.serialization.Serializable

@Serializable
data class DeleteSessionResponse(
    val success: Boolean
)