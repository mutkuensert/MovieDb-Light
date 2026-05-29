package core.data.network.interceptor

import core.domain.ApiKeyManager
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor(private val apiKeyManager: ApiKeyManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val baseRequest = chain.request()
        val url = baseRequest
            .url
            .newBuilder()
            .addQueryParameter("api_key", apiKeyManager.tmdbApiKey)
            .build()
        val newRequest = baseRequest.newBuilder().url(url).build()
        return chain.proceed(newRequest)
    }
}