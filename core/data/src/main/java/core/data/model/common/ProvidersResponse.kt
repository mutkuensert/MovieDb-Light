package core.data.model.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProvidersResponse(
    val id: Int,
    val results: Map<String, ProvidersDto>
)

@Serializable
data class ProvidersDto(
    val link: String,
    val flatrate: List<ProviderDto>? = null,
    val rent: List<ProviderDto>? = null,
    val buy: List<ProviderDto>? = null
)

@Serializable
data class ProviderDto(
    @SerialName("logo_path")
    val logoPath: String?,
    @SerialName("provider_id")
    val providerId: Int,
    @SerialName("provider_name")
    val providerName: String,
    @SerialName("display_priority")
    val displayPriority: Int?
)
