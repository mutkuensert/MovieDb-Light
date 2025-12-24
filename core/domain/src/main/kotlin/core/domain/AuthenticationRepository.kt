package core.domain

import com.github.michaelbull.result.Result

interface AuthenticationRepository {
    suspend fun getRequestToken(): Result<String, Failure>
    suspend fun startSession(): Result<Unit, Failure>
    suspend fun logout(): Result<Unit, Failure>
}