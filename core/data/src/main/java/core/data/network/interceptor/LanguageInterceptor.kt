package core.data.network.interceptor

import core.database.LanguagePreference
import okhttp3.Interceptor
import okhttp3.Response

class LanguageInterceptor(
    private val languagePreference: LanguagePreference,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val newUrl = request.url.newBuilder()
            .setQueryParameter(
                "language",
                languagePreference.get().toLanguageTag()
            )
            .build()

        val newRequest = request.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }
}
