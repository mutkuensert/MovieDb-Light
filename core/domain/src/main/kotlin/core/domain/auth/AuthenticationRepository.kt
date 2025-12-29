package core.domain.auth

import com.github.michaelbull.result.Result
import core.domain.Failure

interface AuthenticationRepository {
    suspend fun getRequestToken(): Result<String, Failure>
    suspend fun startSession(): Result<Unit, Failure>
    suspend fun logout(): Result<Unit, Failure>
}