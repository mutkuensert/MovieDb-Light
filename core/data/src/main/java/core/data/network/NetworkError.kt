package core.data.network

import core.domain.AuthFailure
import core.domain.Failure
import core.domain.UndefinedFailure

class NetworkError(val httpCode: Int?, val statusCode: Int?, val message: String)

fun NetworkError.toFailure(): Failure {
    return when (this.httpCode) {
        HttpCode.UNAUTHORIZED.code -> AuthFailure(this.message)
        else -> UndefinedFailure(this.message)
    }
}