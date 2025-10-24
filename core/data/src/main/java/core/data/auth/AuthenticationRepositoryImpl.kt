package core.data.auth

import android.content.Context
import android.content.Intent
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.get
import com.github.michaelbull.result.mapBoth
import com.github.michaelbull.result.mapOr
import core.data.SessionManagerImpl
import core.domain.AuthenticationRepository
import core.domain.ErrorMessage

const val APP_DEEP_LINK = "mutkuensert.moviedblight://app/"

class AuthenticationRepositoryImpl(
    private val context: Context,
    private val authenticationService: AuthenticationService,
    private val sessionManager: SessionManagerImpl,
) : AuthenticationRepository {

    override suspend fun getRequestToken(): Result<String, ErrorMessage> {
        return authenticationService.getRequestToken()
            .mapBoth(
                success = {
                    Ok(it.requestToken)
                }, failure = {
                    Err("Unsuccessful request token")
                })
    }

    suspend fun login() {
        val requestToken = getRequestToken().get()

        if (requestToken != null) {
            openLoginPage(requestToken)
        }
    }

    private fun openLoginPage(requestToken: String) {
        val intent = CustomTabsIntent.Builder()
            .setShareState(CustomTabsIntent.SHARE_STATE_OFF)
            .build()

        intent.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        var uri =
            ("https://www.themoviedb.org/authenticate/" +
                    requestToken +
                    "?redirect_to" +
                    "=$APP_DEEP_LINK").toUri()
        if (uri.scheme == null) {
            uri = uri
                .buildUpon()
                .scheme("https")
                .build()
        }

        intent.launchUrl(context, uri)
    }

    override suspend fun startSession(requestToken: String): Result<String, ErrorMessage> {
        return authenticationService.startSession(NewSessionRequest(requestToken))
            .mapBoth(success = { Ok(it.sessionId) }, failure = {
                Err("Unsuccessful request token validation.")
            })
    }

    override suspend fun logout(): Boolean {
        val sessionId = requireNotNull(sessionManager.getSessionId())

        return authenticationService.deleteSession(SessionIdRequest(sessionId))
            .mapOr(
                default = false,
                transform = {
                    it.success
                })
    }
}