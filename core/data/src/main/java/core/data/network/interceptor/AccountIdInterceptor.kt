package core.data.network.interceptor

import core.database.user.UserManager
import okhttp3.Interceptor
import okhttp3.Response

class AccountIdInterceptor(private val userManager: UserManager) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val baseRequest = chain.request()

        val urlBuilder = baseRequest.url.newBuilder()
        val segments = baseRequest.url.pathSegments

        for (index in segments.indices) {
            if ("account_id" == segments[index]) {
                val accountId = userManager.getUser()?.id?.toString()
                    ?: throw RuntimeException("Account id could not be found.")

                urlBuilder.setPathSegment(index, accountId)
            }
        }

        val newRequest = baseRequest.newBuilder()
            .url(urlBuilder.build())
            .build()

        return chain.proceed(newRequest)
    }
}