package core.data.network

import libraries.stringresource.StrResource
import moviedblight.core.data.R

internal class HttpErrorCodeMessageProvider(private val strResource: StrResource) {
    fun getUserFriendlyMessage(code: Int): String {
        return when (code) {
            400 -> strResource.get(R.string.error_400_bad_request)
            401 -> strResource.get(R.string.error_401_unauthorized)
            403 -> strResource.get(R.string.error_403_forbidden)
            404 -> strResource.get(R.string.error_404_not_found)
            408 -> strResource.get(R.string.error_408_request_timeout)
            429 -> strResource.get(R.string.error_429_too_many_requests)
            500 -> strResource.get(R.string.error_500_internal_server)
            502 -> strResource.get(R.string.error_502_bad_gateway)
            503 -> strResource.get(R.string.error_503_service_unavailable)
            504 -> strResource.get(R.string.error_504_gateway_timeout)
            else -> strResource.get(R.string.undefined_status_code)
        }
    }
}