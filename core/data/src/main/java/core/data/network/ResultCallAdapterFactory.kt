package core.data.network

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import kotlinx.serialization.json.Json
import filmcan.core.data.R
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import timber.log.Timber
import utils.stringresource.StringResource
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import javax.net.ssl.SSLPeerUnverifiedException

internal class ResultCallAdapterFactory(
    private val json: Json,
    private val stringResource: StringResource,
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
        return ResultCallAdapter<Any>(responseType, json, stringResource)
    }
}

private class ResultCallAdapter<T>(
    private val type: Type,
    private val json: Json,
    private val stringResource: StringResource
) : CallAdapter<T, Call<Result<T, NetworkError>>> {

    override fun responseType(): Type {
        return type
    }

    override fun adapt(call: Call<T>): Call<Result<T, NetworkError>> {
        return ResultCall(call, type, json, stringResource)
    }
}

private class ResultCall<T>(
    private val call: Call<T>,
    private val successType: Type,
    private val json: Json,
    private val stringResource: StringResource,
) : Call<Result<T, NetworkError>> {

    override fun enqueue(callback: Callback<Result<T, NetworkError>>) {
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
                val error = when (throwable) {
                    is SSLPeerUnverifiedException -> {
                        val sslCertificateError = 495
                        Err(
                            NetworkError(
                                httpCode = sslCertificateError,
                                statusCode = null,
                                message = stringResource.get(R.string.something_is_wrong)
                            )
                        )
                    }

                    else -> {
                        Err(
                            NetworkError(
                                httpCode = null,
                                statusCode = null,
                                stringResource.get(R.string.unknown_request_error)
                            )
                        )
                    }
                }
                Timber.e(throwable)
                callback.onResponse(this@ResultCall, Response.success(error))
            }
        }
        )
    }

    fun <T> Response<T>.toResult(successType: Type): Result<T, NetworkError> {
        if (!isSuccessful) {
            val error = try {
                errorBody()?.let {
                    json.decodeFromString<ErrorResponse>(it.string())
                }
            } catch (exception: Exception) {
                Timber.e("Error body couldn't be deserialized:\n${exception.stackTraceToString()}")
                null
            }
            if (error != null) {
                Timber.e(
                    "Status Code: ${error.statusCode} " +
                            "Status Message: ${error.statusMessage}"
                )
            }

            val userFriendlyMessage = HttpErrorCodeMessageProvider(stringResource)
                .getUserFriendlyMessage(code())
            return Err(
                NetworkError(
                    code(),
                    error?.statusCode,
                    error?.statusMessage ?: userFriendlyMessage
                )
            )
        }

        body()?.let { body -> return Ok(body) }

        return if (successType == Unit::class.java) {
            @Suppress("UNCHECKED_CAST")
            Ok(Unit as T)
        } else {
            throw RuntimeException("Response body is null. This must be handled.")
        }
    }

    override fun clone(): Call<Result<T, NetworkError>> =
        ResultCall(call.clone(), successType, json, stringResource)

    override fun execute(): Response<Result<T, NetworkError>> =
        throw UnsupportedOperationException()

    override fun isExecuted(): Boolean = call.isExecuted

    override fun cancel() = call.cancel()

    override fun isCanceled(): Boolean = call.isCanceled

    override fun request(): Request = call.request()

    override fun timeout(): Timeout = call.timeout()
}
