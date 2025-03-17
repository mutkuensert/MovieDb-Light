package core.data.network

import core.data.StrResources
import moviedblight.core.data.R

internal class HttpErrorCodeMessageProvider(private val strResources: StrResources) {
    fun getUserFriendlyMessage(code: Int): String {
        return when (code) {
            400 -> strResources.get(R.string.error_400_bad_request)
            401 -> strResources.get(R.string.error_401_unauthorized)
            403 -> strResources.get(R.string.error_403_forbidden)
            404 -> strResources.get(R.string.error_404_not_found)
            408 -> strResources.get(R.string.error_408_request_timeout)
            429 -> strResources.get(R.string.error_429_too_many_requests)
            500 -> strResources.get(R.string.error_500_internal_server)
            502 -> strResources.get(R.string.error_502_bad_gateway)
            503 -> strResources.get(R.string.error_503_service_unavailable)
            504 -> strResources.get(R.string.error_504_gateway_timeout)
            else -> strResources.get(R.string.undefined_status_code)
        }
    }
}