package core.data.network.interceptor

import core.data.auth.LogoutTrigger
import core.data.network.ErrorResponse
import core.database.user.UserManager
import filmcan.core.data.R
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import utils.stringresource.StringResource

class AccountIdInterceptor(
    private val userManager: UserManager,
    private val logoutTrigger: LogoutTrigger,
    private val json: Json,
    private val stringResource: StringResource,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val urlBuilder = request.url.newBuilder()
        val segments = request.url.pathSegments

        for (index in segments.indices) {
            if ("account_id" == segments[index]) {
                val accountId = userManager.getUser()?.id?.toString()
                if (accountId == null) {
                    logoutTrigger.triggerLogout()

                    return Response.Builder()
                        .request(request)
                        .protocol(Protocol.HTTP_1_1)
                        .code(401)
                        .message("Unauthorized")
                        .body(
                            json.encodeToString(
                                ErrorResponse(
                                    401,
                                    stringResource.get(R.string.logged_out_unknown_reason)
                                )
                            )
                                .toResponseBody("application/json".toMediaType())
                        )
                        .build()
                } else {
                    urlBuilder.setPathSegment(index, accountId)
                }
            }
        }

        val newRequest = request.newBuilder()
            .url(urlBuilder.build())
            .build()

        return chain.proceed(newRequest)
    }
}