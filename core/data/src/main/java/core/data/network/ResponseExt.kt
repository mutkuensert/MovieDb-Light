package core.data.network

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Response
import timber.log.Timber
import java.lang.reflect.Type

fun <T> Response<T>.toNetworkResult(
    successType: Type,
    errorConverter: Converter<ResponseBody, Result<Nothing, Failure>>
): NetworkResult<T> {
    if (!isSuccessful) {
        var error: NetworkResult<T>? = null

        try {
            error = errorBody()?.let { errorConverter.convert(it) }
        } catch (exception: Exception) {
            Timber.e(
                "Error body couldn't be deserialized:\n" +
                        exception.stackTraceToString()
            )
        }

        if (code() == 404) {
            return Err(Failure("404 Not Found")) //TODO("Implement better error handling")
        }

        return error ?: Err(Failure("Unknown Error"))
    }

    // Http success response with body
    body()?.let { body -> return Ok(body) }

    // if we defined Unit as success type it means we expected no response body
    // e.g. in case of 204 No Content
    return if (successType == Unit::class.java) {
        @Suppress("UNCHECKED_CAST")
        Ok(Unit as T)
    } else {
        Err(Failure("Response body was null"))
    }
}
