package core.data.network.interceptor

import core.data.RemoteConfig
import core.data.network.ErrorResponse
import core.domain.ApiKeyManager
import filmcan.core.data.R
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import utils.stringresource.StringResource

class ApiKeyInterceptor(
    private val apiKeyManager: ApiKeyManager,
    private val json: Json,
    private val stringResource: StringResource,
    private val remoteConfig: RemoteConfig,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (apiKeyManager.tmdbApiKey.value == null) {
            remoteConfig.fetch(
                onSuccess = { apiKey ->
                    apiKeyManager.tmdbApiKey.value = apiKey
                },
                onFailure = {}
            )
        }
        val apiKey: String? = runBlocking {
            apiKeyManager.tmdbApiKey.first { it != null }
        }

        return if (apiKey == null) {
            Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(401)
                .message("Unauthorized")
                .body(
                    json.encodeToString(
                        ErrorResponse(
                            401,
                            stringResource.get(R.string.api_key_could_not_be_retrieved)
                        )
                    )
                        .toResponseBody("application/json".toMediaType())
                )
                .build()
        } else {
            val url = request
                .url
                .newBuilder()
                .addQueryParameter("api_key", apiKey)
                .build()
            val newRequest = request.newBuilder().url(url).build()
            chain.proceed(newRequest)
        }
    }
}