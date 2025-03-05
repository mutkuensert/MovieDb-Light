package core.data.network

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import core.data.StringResProvider
import javax.net.ssl.SSLPeerUnverifiedException
import kotlinx.serialization.json.Json
import moviedblight.core.data.R
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import timber.log.Timber
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

internal class ResultCallAdapterFactory(
    private val json: Json,
    private val stringResProvider: StringResProvider,
) : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != Call::class.java) return null
        check(returnType is ParameterizedType) { "Return type must be a parameterized type." }

        val type = getParameterUpperBound(0, returnType)
        check(type is ParameterizedType) { "Return type must be a parameterized type." }
        if (getRawType(type) != Result::class.java) return null

        val responseType = getParameterUpperBound(0, type)
        return ResultCallAdapter<Any>(responseType, json, stringResProvider)
    }
}

private class ResultCallAdapter<T>(
    private val type: Type,
    private val json: Json,
    private val stringResProvider: StringResProvider
) : CallAdapter<T, Call<Result<T, Failure>>> {

    override fun responseType(): Type {
        return type
    }

    override fun adapt(call: Call<T>): Call<Result<T, Failure>> {
        return ResultCall(call, type, json, stringResProvider)
    }
}

private class ResultCall<T>(
    private val call: Call<T>,
    private val successType: Type,
    private val json: Json,
    private val stringResProvider: StringResProvider,
) : Call<Result<T, Failure>> {

    override fun enqueue(callback: Callback<Result<T, Failure>>) {
        call.enqueue(object : Callback<T> {

            override fun onResponse(
                call: Call<T>,
                response: Response<T>
            ) {
                callback.onResponse(
                    this@ResultCall,
                    Response.success(response.toResult(successType))
                )
            }

            override fun onFailure(call: Call<T>, throwable: Throwable) {
                val error = if (throwable is SSLPeerUnverifiedException) {
                    Err(Failure(stringResProvider.get(R.string.something_is_wrong)))
                } else {
                    Err(Failure(stringResProvider.get(R.string.unknown_request_error)))
                }
                Timber.e(throwable)
                callback.onResponse(this@ResultCall, Response.success(error))
            }
        }
        )
    }

    fun <T> Response<T>.toResult(successType: Type): Result<T, Failure> {
        if (!isSuccessful) {
            try {
                val error = errorBody()?.let {
                    json.decodeFromString<ErrorResponse>(it.string())
                }
                if (error != null) {
                    Timber.e(
                        "Status Code: ${error.statusCode} " +
                                "Status Message: ${error.statusMessage}"
                    )
                }
            } catch (exception: Exception) {
                Timber.e("Error body couldn't be deserialized:\n${exception.stackTraceToString()}")
            }

            val userFriendlyMessage = HttpCodeMessageProvider(stringResProvider)
                .getUserFriendlyMessage(code())
            return Err(Failure(userFriendlyMessage))
        }

        body()?.let { body -> return Ok(body) }

        // if we defined Unit as success type it means we expected no response body
        // e.g. in case of 204 No Content
        return if (successType == Unit::class.java) {
            @Suppress("UNCHECKED_CAST")
            Ok(Unit as T)
        } else {
            throw RuntimeException("Response body is null. This must be handled.")
        }
    }

    override fun clone(): Call<Result<T, Failure>> =
        ResultCall(call.clone(), successType, json, stringResProvider)

    override fun execute(): Response<Result<T, Failure>> = throw UnsupportedOperationException()

    override fun isExecuted(): Boolean = call.isExecuted

    override fun cancel() = call.cancel()

    override fun isCanceled(): Boolean = call.isCanceled

    override fun request(): Request = call.request()

    override fun timeout(): Timeout = call.timeout()
}
