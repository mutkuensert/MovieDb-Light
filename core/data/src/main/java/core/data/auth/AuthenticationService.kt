package core.data.auth

import core.data.network.NetworkResult
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST

interface AuthenticationService {

    @GET("authentication/token/new")
    suspend fun getRequestToken(): NetworkResult<RequestTokenResponse>

    @POST("authentication/session/new")
    suspend fun startSession(
        @Body newSessionRequest: NewSessionRequest
    ): NetworkResult<SessionResponse>

    @HTTP(method = "DELETE", path = "authentication/session", hasBody = true)
    suspend fun deleteSession(@Body sessionIdRequest: SessionIdRequest): NetworkResult<DeleteSessionResponse>
}