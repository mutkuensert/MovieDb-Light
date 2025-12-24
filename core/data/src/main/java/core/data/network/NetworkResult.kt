package core.data.network

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.annotation.UnsafeResultValueAccess
import com.github.michaelbull.result.mapBoth
import core.domain.Failure

typealias NetworkResult<T> = Result<T, NetworkError>

@OptIn(UnsafeResultValueAccess::class)
fun <T, R> NetworkResult<T>.mapToDomain(transform: (T) -> R): Result<R, Failure> {
    return mapBoth(
        success = { Ok(transform(value)) },
        failure = { networkError ->
            Err(networkError.toFailure())
        }
    )
}