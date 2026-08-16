package core.data.network.interceptor

import core.data.network.ErrorResponse
import core.domain.apikey.ApiKeyState
import core.domain.apikey.ApiKeyStateHandler
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
    private val apiKeyStateHandler: ApiKeyStateHandler,
    private val json: Json,
    private val stringResource: StringResource,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val apiKey: ApiKeyState = runBlocking {
            apiKeyStateHandler.tmdbApiKey.first { it != ApiKeyState.NotRequested() }
        }

        return if (apiKey is ApiKeyState.Success) {
            val url = request
                .url
                .newBuilder()
                .addQueryParameter("api_key", apiKey.key)
                .build()
            val newRequest = request.newBuilder().url(url).build()
            chain.proceed(newRequest)
        } else {
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
        }
    }
}