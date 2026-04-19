package core.data.network.interceptor

import android.content.Context
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.security.ProviderInstaller
import core.data.network.ProviderInstallerException
import core.data.network.SecurityProviderStateManager
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

class SecurityProviderInterceptor(
    private val context: Context,
    private val securityProviderStateManager: SecurityProviderStateManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (securityProviderStateManager.isChecked) return chain.proceed(chain.request())
        checkSecurityProvider()
        return chain.proceed(chain.request())
    }

    private fun checkSecurityProvider() {
        try {
            ProviderInstaller.installIfNeeded(context)
            securityProviderStateManager.isChecked = true
        } catch (e: GooglePlayServicesRepairableException) {
            // Indicates that Google Play services is out of date, disabled, etc.
            // Prompt the user to install/update/enable Google Play services.
            GoogleApiAvailability.getInstance()
                .showErrorNotification(context, e.connectionStatusCode)

            Timber.e(e)
            throw ProviderInstallerException("Google Play services is out of date or disabled")

        } catch (e: GooglePlayServicesNotAvailableException) {
            // Indicates a non-recoverable error; the ProviderInstaller can't
            // install an up-to-date Provider.
            Timber.e(e)
            throw ProviderInstallerException("Non-recoverable Google Play services error")
        }
    }
}
