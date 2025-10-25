package core.data.auth

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapBoth
import com.github.michaelbull.result.mapOr
import com.github.michaelbull.result.onSuccess
import core.data.SessionManager
import core.domain.AuthenticationRepository
import core.domain.ErrorMessage
import core.libraries.StrResources
import moviedblight.core.data.R

class AuthenticationRepositoryImpl(
    private val authenticationService: AuthenticationService,
    private val sessionManager: SessionManager,
    private val strResources: StrResources,
) : AuthenticationRepository {

    override suspend fun getRequestToken(): Result<String, ErrorMessage> {
        val requestToken = sessionManager.getRequestToken()
        return if (requestToken != null) {
            Ok(requestToken)
        } else {
            authenticationService.getRequestToken()
                .mapBoth(
                    success = {
                        sessionManager.setRequestToken(it.requestToken)
                        Ok(it.requestToken)
                    }, failure = {
                        Err("Unsuccessful request token")
                    })
        }
    }

    override suspend fun startSession(): Result<Unit, ErrorMessage> {
        val requestToken = sessionManager.getRequestToken()
            ?: return Err(strResources.get(R.string.something_is_wrong))

        return authenticationService.startSession(NewSessionRequest(requestToken))
            .mapBoth(success = {
                sessionManager.setSessionId(it.sessionId)
                sessionManager.removeRequestToken()
                Ok(Unit)
            }, failure = {
                Err("Unsuccessful request token validation.")
            })
    }

    override suspend fun logout(): Boolean {
        val sessionId = requireNotNull(sessionManager.getSessionId()) {
            "Session id can't be null if logout can be called."
        }

        return authenticationService.deleteSession(SessionIdRequest(sessionId))
            .onSuccess {
                if (it.success) {
                    sessionManager.removeSessionId()
                }
            }
            .mapOr(
                default = false,
                transform = {
                    it.success
                })
    }
}