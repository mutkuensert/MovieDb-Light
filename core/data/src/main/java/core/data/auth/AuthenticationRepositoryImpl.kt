package core.data.auth

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapBoth
import com.github.michaelbull.result.onOk
import core.data.SessionManager
import core.data.network.toFailure
import core.domain.Failure
import core.domain.UndefinedFailure
import core.domain.auth.AuthenticationRepository
import filmcan.core.data.R
import timber.log.Timber
import utils.stringresource.StringResource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticationRepositoryImpl @Inject constructor(
    private val authenticationService: AuthenticationService,
    private val sessionManager: SessionManager,
    private val stringResource: StringResource,
) : AuthenticationRepository {

    override suspend fun getRequestToken(): Result<String, Failure> {
        return authenticationService.getRequestToken()
            .mapBoth(
                success = {
                    sessionManager.setRequestToken(it.requestToken)
                    Ok(it.requestToken)
                }, failure = {
                    Timber.w("Unsuccessful request token")
                    Err(it.toFailure())
                })
    }

    override suspend fun startSession(): Result<Unit, Failure> {
        val requestToken = sessionManager.getRequestToken()
            ?: return Err(UndefinedFailure(stringResource.get(R.string.something_is_wrong)))

        return authenticationService.startSession(NewSessionRequest(requestToken))
            .mapBoth(success = {
                sessionManager.setSessionId(it.sessionId)
                sessionManager.removeRequestToken()
                Ok(Unit)
            }, failure = { networkError ->
                Timber.w("Unsuccessful request token validation.")
                Err(networkError.toFailure())
            })
    }

    override suspend fun logout(): Result<Unit, Failure> {
        val sessionId = sessionManager.requireSessionId()
        return authenticationService.deleteSession(SessionIdRequest(sessionId))
            .onOk {
                if (it.success) {
                    sessionManager.removeSessionId()
                }
            }
            .mapBoth(
                success = { response ->
                    if (response.success) {
                        Ok(Unit)
                    } else {
                        Err(UndefinedFailure(stringResource.get(R.string.logout_attempt_has_failed)))
                    }
                },
                failure = { networkError ->
                    Err(networkError.toFailure())
                })
    }
}
