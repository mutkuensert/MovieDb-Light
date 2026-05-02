package core.data.network

import utils.stringresource.StringResource
import filmcan.core.data.R

internal class HttpErrorCodeMessageProvider(private val stringResource: StringResource) {
    fun getUserFriendlyMessage(code: Int): String {
        return when (code) {
            400 -> stringResource.get(R.string.error_400_bad_request)
            401 -> stringResource.get(R.string.error_401_unauthorized)
            403 -> stringResource.get(R.string.error_403_forbidden)
            404 -> stringResource.get(R.string.error_404_not_found)
            408 -> stringResource.get(R.string.error_408_request_timeout)
            429 -> stringResource.get(R.string.error_429_too_many_requests)
            500 -> stringResource.get(R.string.error_500_internal_server)
            502 -> stringResource.get(R.string.error_502_bad_gateway)
            503 -> stringResource.get(R.string.error_503_service_unavailable)
            504 -> stringResource.get(R.string.error_504_gateway_timeout)
            else -> stringResource.get(R.string.undefined_status_code)
        }
    }
}