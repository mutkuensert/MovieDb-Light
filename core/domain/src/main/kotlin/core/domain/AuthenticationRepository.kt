package core.domain

import com.github.michaelbull.result.Result

interface AuthenticationRepository {
    suspend fun getRequestToken(): Result<String, ErrorMessage>
    suspend fun startSession(): Result<Unit, ErrorMessage>
    suspend fun logout(): Result<Unit, ErrorMessage>
}