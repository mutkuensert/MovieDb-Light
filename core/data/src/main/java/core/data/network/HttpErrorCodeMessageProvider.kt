package core.data.network

import core.data.StringResProvider
import moviedblight.core.data.R

internal class HttpErrorCodeMessageProvider(private val stringResProvider: StringResProvider) {
    fun getUserFriendlyMessage(code: Int): String {
        return when (code) {
            400 -> stringResProvider.get(R.string.error_400_bad_request)
            401 -> stringResProvider.get(R.string.error_401_unauthorized)
            403 -> stringResProvider.get(R.string.error_403_forbidden)
            404 -> stringResProvider.get(R.string.error_404_not_found)
            408 -> stringResProvider.get(R.string.error_408_request_timeout)
            429 -> stringResProvider.get(R.string.error_429_too_many_requests)
            500 -> stringResProvider.get(R.string.error_500_internal_server)
            502 -> stringResProvider.get(R.string.error_502_bad_gateway)
            503 -> stringResProvider.get(R.string.error_503_service_unavailable)
            504 -> stringResProvider.get(R.string.error_504_gateway_timeout)
            else -> stringResProvider.get(R.string.undefined_status_code)
        }
    }
}