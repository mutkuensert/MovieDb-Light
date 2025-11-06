package core.data.auth

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapBoth
import com.github.michaelbull.result.onSuccess
import core.data.SessionManager
import core.domain.AuthenticationRepository
import core.domain.ErrorMessage
import libraries.StrResource
import moviedblight.core.data.R

class AuthenticationRepositoryImpl(
    private val authenticationService: AuthenticationService,
    private val sessionManager: SessionManager,
    private val strResource: StrResource,
) : AuthenticationRepository {

    override suspend fun getRequestToken(): Result<String, ErrorMessage> {
        return authenticationService.getRequestToken()
            .mapBoth(
                success = {
                    sessionManager.setRequestToken(it.requestToken)
                    Ok(it.requestToken)
                }, failure = {
                    Err("Unsuccessful request token")
                })
    }

    override suspend fun startSession(): Result<Unit, ErrorMessage> {
        val requestToken = sessionManager.getRequestToken()
            ?: return Err(strResource.get(R.string.something_is_wrong))

        return authenticationService.startSession(NewSessionRequest(requestToken))
            .mapBoth(success = {
                sessionManager.setSessionId(it.sessionId)
                sessionManager.removeRequestToken()
                Ok(Unit)
            }, failure = {
                Err("Unsuccessful request token validation.")
            })
    }

    override suspend fun logout(): Result<Unit, ErrorMessage> {
        val sessionId = requireNotNull(sessionManager.getSessionId()) {
            "Session id can't be null if logout can be called."
        }

        return authenticationService.deleteSession(SessionIdRequest(sessionId))
            .onSuccess {
                if (it.success) {
                    sessionManager.removeSessionId()
                }
            }
            .mapBoth(
                success = { response ->
                    if (response.success) {
                        Ok(Unit)
                    } else {
                        Err(strResource.get(R.string.logout_attempt_has_failed))
                    }
                },
                failure = { error ->
                    Err(error.message)
                })
    }
}