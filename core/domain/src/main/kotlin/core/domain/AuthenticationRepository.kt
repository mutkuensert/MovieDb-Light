package core.domain

import com.github.michaelbull.result.Result

interface AuthenticationRepository {
    suspend fun getRequestToken(): Result<String, ErrorMessage>
    suspend fun startSession(requestToken: String): Result<String, ErrorMessage>
    suspend fun logout(): Boolean
}